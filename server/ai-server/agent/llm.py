from dotenv import load_dotenv
from langchain_google_genai import ChatGoogleGenerativeAI

if __name__ == "__main__":

    load_dotenv()

    # ChatGoogleGenerativeAI 인스턴스 생성
    llm = ChatGoogleGenerativeAI(
        model="gemini-2.5-flash",  # 사용할 Google AI 모델 이름
        temperature=0,           # 생성 결과의 다양성 제어
        max_output_tokens=200,     # 최대 생성 토큰 수
    )
    #
    # # 간단한 프롬프트 보내기
    messages = [
        (
            "system",
            "You are a helpful assistant that translates English to French. Translate the user sentence.",
        ),
        ("human", "I love programming."),
    ]
    ai_msg = llm.invoke(messages)
    print("AI 응답:", ai_msg.content)
