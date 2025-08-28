import time
import logging
from typing import Dict, Any
from agent.basic_agent import get_agent_executor, summarizer
from router.app import UserMessage

# memory saver : 추후 redis 사용 >> 직접 구현 필요
from langgraph.checkpoint.memory import MemorySaver

# 로깅 설정
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# setting
agent_executor = get_agent_executor()
def generate_ai_response(user_message: UserMessage) -> Dict[str, Any]:
    start = time.time()

    # ai 생성 로직
    event = agent_executor.invoke(
        {"messages": [("user", user_message.message)]}
    )
    print(event)
    final_state = summarizer(event)["summarized_messages"][-1]
    end = time.time()
    logger.info(f"[generate_ai_response] generate time : {end-start}ms")
    print(final_state)
    return {
        "sessionId": user_message.session_id,
        "message": f"AI 응답: {final_state}",
        "userId": user_message.user_id,
        "target": "LLM",
        "createdAt": user_message.created_at
    }

