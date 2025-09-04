from typing import Any, Annotated

from dotenv import load_dotenv
from langchain_community.agent_toolkits import SQLDatabaseToolkit
from langchain_community.utilities import SQLDatabase
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.runnables import RunnableWithFallbacks, RunnableLambda
from langgraph.prebuilt import ToolNode
from typing_extensions import TypedDict, Literal
from langchain_core.messages import ToolMessage, AnyMessage, AIMessage
from langchain_core.tools import tool
from langgraph.constants import END, START
from langgraph.graph import add_messages, StateGraph
from pydantic import BaseModel, Field

from util import get_llm

DB_URI = ""

load_dotenv()
llm = get_llm()
db = SQLDatabase.from_uri(DB_URI)
toolkit = SQLDatabaseToolkit(db=db, llm=llm)
tools = toolkit.get_tools()
list_tables_tool = next(tool for tool in tools if tool.name == "sql_db_list_tables")
get_schema_tool = next(tool for tool in tools if tool.name == "sql_db_schema")

@tool
def db_query_tool(query: str) -> str:
    """
    Execute a SQL query against the database and get back the result.
    If the query is not correct, an error message will be returned.
    If an error is returned, rewrite the query, check the query, and try again.
    """
    result = db.run_no_throw(query)
    if not result:
        return "Error: Query failed. Please rewrite your query and try again."
    return result

def create_tool_node_with_fallback(tools: list) -> RunnableWithFallbacks[Any, dict]:
    """
    Create a ToolNode with a fallback to handle errors and surface them to the agent.
    """
    return ToolNode(tools).with_fallbacks(
        [RunnableLambda(handle_tool_error)], exception_key="error"
    )

def handle_tool_error(state) -> dict:
    error = state.get("error")
    tool_calls = state["messages"][-1].tool_calls
    return {
        "messages": [
            ToolMessage(
                content=f"Error: {repr(error)}\n please fix your mistakes.",
                tool_call_id=tc["id"],
            )
            for tc in tool_calls
        ]
    }


class State(TypedDict):
    messages: Annotated[list[AnyMessage], add_messages]

class SubmitFinalAnswer(BaseModel):
    """Submit the final answer to the user based on the query results."""

    final_answer: str = Field(..., description="The final answer to the user")


def query_check():
    query_check_system = """You are a SQL expert with a strong attention to detail.
    Double check the PostgreSQL query for common mistakes, including:
    - Using NOT IN with NULL values
    - Using UNION when UNION ALL should have been used
    - Using BETWEEN for exclusive ranges
    - Data type mismatch in predicates
    - Properly quoting identifiers
    - Using the correct number of arguments for functions
    - Casting to the correct data type
    - Using the proper columns for joins
    If there are any of the above mistakes, rewrite the query. If there are no mistakes, just reproduce the original query.
    You will call the appropriate tool to execute the query after running this check."""

    query_check_prompt = ChatPromptTemplate.from_messages([
        {"role": "system", "content": query_check_system},
        {"role": "user", "content": "{messages}"}
    ])
    query_check = query_check_prompt | llm.bind_tools([db_query_tool])
    return query_check

def first_tool_call(state: State) -> dict[str, list[AIMessage]]:
    return {
        "messages": [AIMessage(
            content="",
            tool_calls=[{
                "name": "sql_db_list_tables",
                "args": {},
                "id": "tool_abcd123", }],
        )]
    }

def model_check_query(state: State) -> dict[str, list[AIMessage]]:
    """`
    Use this tool to double-check if your query is correct before executing it.
    """
    query_check_runnable = query_check()
    return {"messages": [query_check_runnable.invoke({"messages": [state["messages"][-1]]})]}


def query_gen():
    accessible_tables = ('hot_article', 'article')
    query_gen_system = f"""
    You are a SQL expert with a strong attention to detail.
    Given an input question, output a syntactically correct SQLite query to run, then look at the results of the query and return the answer.
    DO NOT call any tool besides SubmitFinalAnswer to submit the final answer.
    Rules:
    1. If the user asks for data from any table other than {accessible_tables}, respond with "Nothing".
    2. Do not attempt to infer or rewrite queries for other tables.
    3. Only generate syntactically correct SQL queries for {accessible_tables}.
    When generating the query:
    Output the SQL query that answers the input question without a tool call.
    Unless the user specifies a specific number of examples they wish to obtain, always limit your query to at most 5 results.
    You can order the results by a relevant column to return the most interesting examples in the database.
    Never query for all the columns from a specific table, only ask for the relevant columns given the question.
    If you get an error while executing a query, rewrite the query and try again.
    If you get an empty result set, you should try to rewrite the query to get a non-empty result set.
    NEVER make stuff up if you don't have enough information to answer the query... just say you don't have enough information.
    If you have enough information to answer the input question, simply invoke the appropriate tool to submit the final answer to the user.
    DO NOT make any DML statements (INSERT, UPDATE, DELETE, DROP etc.) to the database.
    """
    query_gen_prompt = ChatPromptTemplate.from_messages(
        [("system", query_gen_system), ("placeholder", "{messages}")]
    )
    query_gen = query_gen_prompt | llm.bind_tools(
        [SubmitFinalAnswer, model_check_query]
    )
    return query_gen

def query_gen_node(state: State):
    query_gen_runnable = query_gen()
    message = query_gen_runnable.invoke(state)

    tool_messages = []
    if message.tool_calls:
        for tc in message.tool_calls:
            if tc["name"] != "SubmitFinalAnswer":
                tool_messages.append(
                    ToolMessage(
                        content=f"Error: The wrong tool was called: {tc['name']}. Please fix your mistakes. Remember to only call SubmitFinalAnswer to submit the final answer. Generated queries should be outputted WITHOUT a tool call.",
                        tool_call_id=tc["id"],
                    )
                )
    else:
        tool_messages = []
    return {"messages": [message] + tool_messages}

def should_continue(state: State) -> Literal[END, "correct_query", "query_gen"]:
    messages = state["messages"]
    last_message = messages[-1]
    # If there is a tool call, then we finish
    if getattr(last_message, "tool_calls", None):
        return END
    if last_message.content.startswith("Error:"):
        return "query_gen"
    else:
        return "correct_query"

def generate_sql_agent_graph():
    workflow = StateGraph(State)

    # node 생성
    workflow.add_node("first_tool_call", first_tool_call)
    workflow.add_node(
        "list_tables_tool", create_tool_node_with_fallback([list_tables_tool])
    )
    workflow.add_node("get_schema_tool", create_tool_node_with_fallback([get_schema_tool]))
    model_get_schema = llm.bind_tools([get_schema_tool])
    workflow.add_node(
        "model_get_schema",
        lambda state: {
            "messages": [model_get_schema.invoke(state["messages"])],
        },
    )
    workflow.add_node("query_gen", query_gen_node)
    workflow.add_node("correct_query", model_check_query)
    workflow.add_node("execute_query", create_tool_node_with_fallback([db_query_tool]))

    # edge 생성
    workflow.add_edge(START, "first_tool_call")
    workflow.add_edge("first_tool_call", "list_tables_tool")
    workflow.add_edge("list_tables_tool", "model_get_schema")
    workflow.add_edge("model_get_schema", "get_schema_tool")
    workflow.add_edge("get_schema_tool", "query_gen")
    workflow.add_conditional_edges(
        "query_gen",
        should_continue,
    )
    workflow.add_edge("correct_query", "execute_query")
    workflow.add_edge("execute_query", "query_gen")

    # compile
    app = workflow.compile()
    return app



if __name__ == "__main__":
    app = generate_sql_agent_graph()
    # messages = app.invoke({
    #     "messages": [
    #         {"role": "user", "content": "조회수가 가장 많은 게시글이 뭐야? 게시글 id랑 content를 알려줘"}
    #     ]
    # })
    messages = app.invoke({
        "messages": [
            {"role": "user", "content": "hot article 1위 게시글의 내용을 알려줘."}
        ]
    })
    answer = messages["messages"][-1].tool_calls[0]["args"]["final_answer"]
    print(answer)

















