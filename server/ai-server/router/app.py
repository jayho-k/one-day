import logging
from dotenv import load_dotenv

import uvicorn
from fastapi import FastAPI
from pydantic import BaseModel

from service import aiservice
from datetime import datetime

# 로깅 설정
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = FastAPI()

class UserMessage(BaseModel):
    session_id: str
    message: str
    user_id: int
    target: str
    created_at: datetime

@app.post("/ai/chat")
def chat(user_message: UserMessage):
    # stream response 방식으로 추후 변경
    res = aiservice.generate_ai_response(user_message)
    print(res)
    return res

if __name__ == "__main__":
    load_dotenv()
    uvicorn.run("router.app:app", host="0.0.0.0", port=9000, reload=True)