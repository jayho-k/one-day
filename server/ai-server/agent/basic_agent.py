import logging
from typing import TypedDict, Annotated, Literal

from langchain_community.tools import DuckDuckGoSearchRun
from langchain_core.prompts import ChatPromptTemplate
from langchain_google_genai import ChatGoogleGenerativeAI
from langgraph.constants import START
from langgraph.graph import add_messages, StateGraph
from langgraph.prebuilt import ToolNode, tools_condition
from dotenv import load_dotenv

# .env 파일 로드
load_dotenv()

# 로깅 설정
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class State(TypedDict):
    messages: Annotated[list, add_messages]

def route_tools(state: State) -> Literal["tools", "__end__"]:
    if isinstance(state, list):
        ai_message = state[-1]
    elif message := state.get("messages", []):
        ai_message = message[-1]
    else:
        raise ValueError("")

    if hasattr(ai_message, "tool_calls") and len(ai_message.tool_calls) > 0:
        return "tools"
    return "__end__"

def get_llm():
    return ChatGoogleGenerativeAI(
        model="gemini-2.5-flash",  # 모델 이름
        temperature=0,
        max_output_tokens=5000,  # 최대 생성 토큰 수
    )

def get_tools()->list:
    search_tool = DuckDuckGoSearchRun()
    return [search_tool]

def create_graph(tools, llm_with_tools, prompt=None):

    def chatbot(state: State):
        if prompt:
            formatted = prompt.format_messages(messages=state["messages"])
            return {"messages": [llm_with_tools.invoke(formatted)]}
        else:
            return {"messages": [llm_with_tools.invoke(state["messages"])]}

    graph_builder = StateGraph(State)
    tool_node = ToolNode(tools)

    # node
    graph_builder.add_node("chatbot", chatbot)
    graph_builder.add_node("tools", tool_node)

    # edge
    graph_builder.add_edge(START, "chatbot")
    graph_builder.add_conditional_edges(
        "chatbot",
        tools_condition,
    )

    # compile
    return graph_builder.compile()

def get_agent_executor():

    custom_prompt = ChatPromptTemplate.from_messages(
        [
            ("system", "You are a helpful search assistant. Always keep your answers brief and to the point."),
            ("user", "{messages}")
         ]
    )
    llm = get_llm()
    tools = get_tools()
    llm_with_tools = llm.bind_tools(tools)
    return create_graph(tools, llm_with_tools, custom_prompt)

def summarizer(event):
    llm = get_llm()
    tool_messages = event["messages"]
    summarized_messages = []
    for msg in tool_messages:
        content = msg.content
        summary_prompt = f"다음 내용을 여러 문장으로 간결하게 한국어로 요약해줘: {content}"
        short_summary = llm.invoke(summary_prompt)
        summarized_messages.append(short_summary.content)
    event["summarized_messages"] = summarized_messages
    return event


