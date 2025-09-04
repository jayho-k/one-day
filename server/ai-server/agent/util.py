from typing import Any

from langchain_core.messages import ToolMessage
from langchain_core.runnables import RunnableWithFallbacks, RunnableLambda
from langchain_google_genai import ChatGoogleGenerativeAI
from langgraph.prebuilt import ToolNode

def get_llm():
    return ChatGoogleGenerativeAI(
        model="gemini-2.5-flash",  # 모델 이름
        temperature=0,
        max_output_tokens=5000,  # 최대 생성 토큰 수
    )


