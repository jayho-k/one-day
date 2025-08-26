import logging
from typing import Dict, Any, Optional

from langchain_core.runnables import RunnablePassthrough
from langchain_milvus import Milvus
from langchain_milvus.utils.sparse import BM25SparseEmbedding
from pymilvus import connections, FieldSchema, DataType, CollectionSchema, Collection, WeightedRanker
from langchain_google_genai import ChatGoogleGenerativeAI
from langchain.schema import HumanMessage
from aimq import kafka_service
from router.app import UserMessage

# 로깅 설정
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


def generate_ai_response(user_message: UserMessage) -> Dict[str, Any]:
    # ai 생성 로직

    llm = ChatGoogleGenerativeAI(
        model="gemini-2.5-flash",  # 모델 이름
        temperature=0,
        max_output_tokens=5000,    # 최대 생성 토큰 수
    )

    ai_msg = llm.invoke([HumanMessage(content=user_message.message)])

    return {
        "sessionId": user_message.session_id,
        "message": f"AI 응답: {ai_msg.content}",
        "userId": user_message.user_id,
        "target": "LLM",
        "createdAt": user_message.created_at
    }



if __name__ == "__main__":
    # practice
    from dotenv import load_dotenv
    from langchain_core.prompts import PromptTemplate
    from langchain_core.output_parsers import StrOutputParser

    load_dotenv()

    texts = [
        "In 'The Whispering Walls' by Ava Moreno, a young journalist named Sophia uncovers a decades-old conspiracy hidden within the crumbling walls of an ancient mansion, where the whispers of the past threaten to destroy her own sanity.",
        "In 'The Last Refuge' by Ethan Blackwood, a group of survivors must band together to escape a post-apocalyptic wasteland, where the last remnants of humanity cling to life in a desperate bid for survival.",
        "In 'The Memory Thief' by Lila Rose, a charismatic thief with the ability to steal and manipulate memories is hired by a mysterious client to pull off a daring heist, but soon finds themselves trapped in a web of deceit and betrayal.",
        "In 'The City of Echoes' by Julian Saint Clair, a brilliant detective must navigate a labyrinthine metropolis where time is currency, and the rich can live forever, but at a terrible cost to the poor.",
        "In 'The Starlight Serenade' by Ruby Flynn, a shy astronomer discovers a mysterious melody emanating from a distant star, which leads her on a journey to uncover the secrets of the universe and her own heart.",
        "In 'The Shadow Weaver' by Piper Redding, a young orphan discovers she has the ability to weave powerful illusions, but soon finds herself at the center of a deadly game of cat and mouse between rival factions vying for control of the mystical arts.",
        "In 'The Lost Expedition' by Caspian Grey, a team of explorers ventures into the heart of the Amazon rainforest in search of a lost city, but soon finds themselves hunted by a ruthless treasure hunter and the treacherous jungle itself.",
        "In 'The Clockwork Kingdom' by Augusta Wynter, a brilliant inventor discovers a hidden world of clockwork machines and ancient magic, where a rebellion is brewing against the tyrannical ruler of the land.",
        "In 'The Phantom Pilgrim' by Rowan Welles, a charismatic smuggler is hired by a mysterious organization to transport a valuable artifact across a war-torn continent, but soon finds themselves pursued by deadly assassins and rival factions.",
        "In 'The Dreamwalker's Journey' by Lyra Snow, a young dreamwalker discovers she has the ability to enter people's dreams, but soon finds herself trapped in a surreal world of nightmares and illusions, where the boundaries between reality and fantasy blur.",
    ]

    # Sparse embedding 생성
    sparse_embedding_func = BM25SparseEmbedding(corpus=texts)
    sparse_embedding_func.embed_query(texts[1])

    # db 연결
    connections.connect("default", host="localhost", port="19530")

    # 컬렉션 스키마 정의
    pk_field = "doc_id"
    sparse_field = "vector"
    text_field = "text"

    fields = [
        FieldSchema(name=pk_field, dtype=DataType.VARCHAR, is_primary=True, auto_id=True, max_length=100),
        FieldSchema(name=sparse_field, dtype=DataType.SPARSE_FLOAT_VECTOR),
        FieldSchema(name=text_field, dtype=DataType.VARCHAR, max_length=65_535),
    ]

    schema = CollectionSchema(fields=fields, enable_dynamic_field=False)

    try:
        collection = Collection(name="test")
        collection.drop()
    except:
        pass

    collection = Collection(name="test", schema=schema, consistency_level="Strong")

    # 인덱스 생성
    sparse_index = {"index_type": "SPARSE_INVERTED_INDEX", "metric_type": "IP"}
    collection.create_index(sparse_field, sparse_index)
    collection.flush()

    # 데이터 삽입
    entities = []
    for text in texts:
        entity = {
            sparse_field: sparse_embedding_func.embed_documents([text])[0],
            text_field: text,
        }
        entities.append(entity)
    collection.insert(entities)
    collection.load()

    # 최신 방식으로 Milvus VectorStore 생성
    vectorstore = Milvus(
        collection_name="test",
        connection_args={"host": "localhost", "port": "19530"},
        embedding_function=sparse_embedding_func,
    )

    # Hybrid retriever 생성
    retriever = vectorstore.as_retriever(
        search_type="similarity",
        search_kwargs={"top_k": 3}
    )

    # RAG 체인 구성
    PROMPT_TEMPLATE = """
    Human: You are an AI assistant, and provides answers to questions by using fact based and statistical information when possible.
    Use the following pieces of information to provide a concise answer to the question enclosed in <question> tags.

    <context>
    {context}
    </context>

    <question>
    {question}
    </question>

    Assistant:"""

    llm = ChatGoogleGenerativeAI(
        model="gemini-2.5-flash",
        temperature=0,
        max_output_tokens=5000,
    )

    prompt = PromptTemplate(
        template=PROMPT_TEMPLATE, input_variables=["context", "question"]
    )

    def format_docs(docs):
        return "\n\n".join(doc.page_content for doc in docs)


    rag_chain = (
            {"context": retriever | format_docs, "question": RunnablePassthrough()}
            | prompt
            | llm
            | StrOutputParser()
    )

    ans = rag_chain.invoke("What novels has Lila written and what are their contents?")
    try:
        print(ans)
    except Exception as e:
        print(e)

    collection.drop()
