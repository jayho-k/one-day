# API 명세서

- 기능
  - user
    - 유저 생성(회원가입), 삭제, 수정, 조회
      - 로그인, 로그아웃
  - article
    - 게시글 생성, 조회(list), 수정, 삭제, 저장 / 정렬(필터)
  - article/like
    - 좋아요 생성, 삭제
  - article/comment
    - 댓글 생성, 조회, 수정, 삭제
  - article/view
    - 조회수 생성, 조회, 수정
  - ai-chat
    - 대화 이력 조회 (일단 단일 chat 방식)
    - 질의 / 답변


그 외 아이디어

- Ground 생성, 조회, 수정, 삭제 (카페)
  - Ground 가입 신청, 신청 취소 (신청자)
  - Ground 가입 수락, 거절(주인장)
  - Ground 신청서 생성, 조회, 수정, 삭제



## Response

### 성공

| 코드        | 내용               |
| ----------- | ------------------ |
| 200 OK      | 요청 성공          |
| 201 Created | 새로운 리소스 생성 |

### 실패

| 코드                      | 내용                         |
| ------------------------- | ---------------------------- |
| 400 Bad Request           | 잘못된 요청                  |
| 401 Unauthorized          | 인증 실패                    |
| 403 Forbidden             | 접근 권한 없음               |
| 404 Not Found             | 요청한 리소스를 찾을 수 없음 |
| 409 Conflict              | 중복 데이터 접근             |
| 500 Internal Server Error | 서버 에러                    |



## User

### 회원 가입

| 항목   | 설명          |
| ------ | ------------- |
| 메소드 | POST          |
| URL    | /user         |
| 설명   | User 회원가입 |

Request

```json
{
    "userName":"jayho",
    "password":"123",
    "email":"jayho@naver.com",
    "sex":"man",
    "profileImage":"image.png",
}
```

Response

```json
// 성공
{
    "status" : 200,
    "message": "회원가입에 성공했습니다."
}
```

```json
// 409 Conflict
{
  "status": 409,
  "message": "이미 가입된 이메일입니다."
}
```



### 조회

| 항목   | 설명                |
| ------ | ------------------- |
| 메소드 | GET                 |
| URL    | /user/{userId}      |
| 설명   | 단일 user 조회 기능 |

Response

```json
// 성공
{
    "status" : 200,
    "message": "회원조회에 성공했습니다."
    "data" :{
        "userId":1,
        "userName":"jayho",
        "email":"000@000.com",
        "sex":"man",
        "profileImage":"image.png"
    }
}
```

```json
// 실패
{
  "status": 404,
  "message": "사용자를 찾을 수 없습니다."
}
```

### 회원 정보 수정

| 항목   | 설명           |
| ------ | -------------- |
| 메소드 | PATCH          |
| URL    | /user/{userId} |
| 설명   | 회원 정보 수정 |
| 헤더   |                |

Response

```json
// 성공
{
    "status" : 200,
    "message": "회원 정보 수정이 완료되었습니다."
    "data" :{
        "userId":1,
        "userName":"jayho",
        "email":"000@000.com",
        "sex":"man",
        "profileImage":"image.png"
    }
}
```

```json
// 실패
{
  "status": 404,
  "message": "사용자를 찾을 수 없습니다."
}
```



### 회원 탈퇴

| 항목   | 설명             |
| ------ | ---------------- |
| 메소드 | PATCH            |
| URL    | /user/{userId}   |
| 설명   | 사용자 회원 탈퇴 |

Response

```json
// 성공
{
    "status" : 200,
    "message": "회원을 탈퇴했습니다."
    "data" :{
        "userId":1,
        "userName":"jayho",
        "email":"000@000.com",
        "sex":"man",
        "profileImage":"image.png"
        "delete":"true"
    }
}
```

```json
// 실패
{
  "status": 404,
  "message": "사용자를 찾을 수 없습니다."
}
```



### 회원 삭제

| 항목   | 설명                    |
| ------ | ----------------------- |
| 메소드 | DELETE                  |
| URL    | /user/{userId}          |
| 설명   | user 회원을 완전히 탈퇴 |

Response

```json
// 성공
{
    "status" : 200,
    "message": "회원삭제에 성공했습니다."
}
```

```json
// 실패
{
  "status": 404,
  "message": "사용자를 찾을 수 없습니다."
}
```





## article

### 게시글 생성

| 항목   | 설명        |
| ------ | ----------- |
| 메소드 | POST        |
| URL    | /article    |
| 설명   | 게시글 생성 |

Request

```json
{	
    "images": [img1,img2 ...]
	"content":"게시글 내용"
    "writerId":1
}
```



Response

```json
// 성공
{
    "status" : 201,
    "message": "게시글 생성에 성공했습니다."
}
```

```json
// 실패
{
  "status": 400,
  "message": "게시글 생성에 실패하였습니다."
}
```



### 게시글 단일 조회

| 항목   | 설명                 |
| ------ | -------------------- |
| 메소드 | GET                  |
| URL    | /article/{articleId} |
| 설명   | 게시글 생성          |

Response

```json
// 성공
{
    "status" : 200,
    "message": "게시글 조회에 성공했습니다."
    "data" :{
        "articleId": 1,
        "images": [img1,img2 ...]
        "content": "게시글 내용입니다.",
        "writerId": 1
        "writerName":"jayho",
        "writerProfileImage":"image.png"
        "createdAt": "2025-06-01T00:00:00",
	    "modifiedAt": "2025-0601T00:00:00",
    }
}
```

```json
// 실패
{
  "status": 404,
  "message": "게시글을 찾을 수 없습니다."
}
```



### 게시글 리스트 조회(정렬)

| 항목         | 설명                    |
| ------------ | ----------------------- |
| 메소드       | GET                     |
| URL          | /article-list/          |
| 설명         | 게시글 생성             |
| RequestParam | pageSize, lastArticleId |

Response

```json
// 성공
{
    "status" : 200,
    "message": "게시글 수정에 성공했습니다."
    "data" :{
    	"articleList": [
    	    {
                "articleId": 1,
             	"images": [img1,img2 ...],
                "content": "게시글 내용입니다.",
                "writerId": 1
                "writerName":"jayho",
                "writerProfileImage":"image.png"
                "createdAt": "2025-06-01T00:00:00",
                "modifiedAt": "2025-0601T00:00:00",
			},
			...
    		]
		}
    }
}
```

```json
// 실패
{
  "status": 404,
  "message": "게시글을 찾을 수 없습니다."
}
```



### 게시글 단일 수정

| 항목         | 설명                 |
| ------------ | -------------------- |
| 메소드       | PATCH                |
| URL          | /article/{articleId} |
| 설명         | 게시글 단일 수정     |
| RequestParam |                      |

Response

```json
// 성공
{
    "status" : 200,
    "message": "게시글 수정에 성공했습니다."
    "data" :{
        "articleId": 1,
      	"images": [img1,img2 ...],
        "content": "게시글 내용입니다.",
        "writerId": 1
        "writerName":"jayho",
        "writerProfileImage":"image.png"
        "createdAt": "2025-06-01T00:00:00",
	    "modifiedAt": "2025-0601T00:00:00",
    }
}
```

```json
// 실패
{
  "status": 404,
  "message": "게시글을 찾을 수 없습니다."
}
```



### 게시글 임시 삭제

| 항목         | 설명                            |
| ------------ | ------------------------------- |
| 메소드       | PATCH                           |
| URL          | /article/tmp-delete/{articleId} |
| 설명         | 게시글 임시 단일 삭제           |
| RequestParam |                                 |

Response

```json
// 성공
{
    "status" : 200,
    "message": "게시글 삭제에 성공했습니다.",
}
```

```json
// 실패
{
  "status": 404,
  "message": "게시글을 찾을 수 없습니다."
}
```



### 게시글 삭제

| 항목         | 설명                 |
| ------------ | -------------------- |
| 메소드       | DELETE               |
| URL          | /article/{articleId} |
| 설명         | 게시글 단일 삭제     |
| RequestParam |                      |

Response

```json
// 성공
{
    "status" : 200,
    "message": "게시글 삭제에 성공했습니다.",
}
```

```json
// 실패
{
  "status": 404,
  "message": "게시글을 찾을 수 없습니다."
}
```



### 게시글 저장

| 항목   | 설명                                    |
| ------ | --------------------------------------- |
| 메소드 | POST                                    |
| URL    | /article/{articleId}/user/{userId}/save |
| 설명   | user가 관심있는 게시글을 저장           |

Request

```json
{
    "userId":1
}
```

Response

```json
// 성공
{
    "status" : 200,
    "message": "게시글 저장에 성공했습니다.",
}
```

```json
// 실패
{
  "status": 404,
  "message": "게시글을 찾을 수 없습니다."
}
```



## 좋아요

### 좋아요 생성

| 항목   | 설명                                             |
| ------ | ------------------------------------------------ |
| 메소드 | POST                                             |
| URL    | /article-likes/article/{articleId}/user/{userId} |
| 설명   | 좋아요 생성                                      |

Response

```json
// 성공
{
    "status" : 200,
    "message": "좋아요 생성에 성공했습니다.",
}
```

```json
// 실패
{
    "status": 404,
    "message": "게시글을 찾을 수 없습니다."
}
```



### 좋아요 삭제

| 항목   | 설명                                             |
| ------ | ------------------------------------------------ |
| 메소드 | DELETE                                           |
| URL    | /article-likes/article/{articleId}/user/{userId} |
| 설명   | 좋아요 삭제                                      |

Response

```json
// 성공
{
    "status" : 200,
    "message": "좋아요 삭제에 성공했습니다.",
}
```

```json
// 실패
{
    "status": 404,
    "message": "게시글을 찾을 수 없습니다."
}
```



### 좋아요 개수 조회

| 항목   | 설명                                     |
| ------ | ---------------------------------------- |
| 메소드 | DELETE                                   |
| URL    | /article-likes/article/{articleId}/count |
| 설명   | 좋아요 개수 조회                         |

Response

```json
// 성공
{
    "status" : 200,
    "message": "좋아요 개수 조회에 성공했습니다.",
    "data" : {
        "articleId":1,
        "count":123
    }
}
```

```json
// 실패
{
    "status": 404,
    "message": "게시글을 찾을 수 없습니다."
}
```





## 댓글

### 댓글 생성

| 항목   | 설명      |
| ------ | --------- |
| 메소드 | POST      |
| URL    | /comment  |
| 설명   | 댓글 생성 |

Request

```json
{
    "articleId":1,
    "content":"댓글 내용",
    "wirterId":1
}
```

Response

```json
// 성공
{
    "status" : 200,
    "message": "댓글 생성에 성공했습니다.",
}
```

```json
// 실패
{
    "status": 404,
    "message": "게시글을 찾을 수 없습니다."
}
```



### 댓글 리스트 조회

| 항목          | 설명                                                 |
| ------------- | ---------------------------------------------------- |
| 메소드        | GET                                                  |
| URL           | comment/scroll                                       |
| 설명          | 댓글 생성                                            |
| Request Param | Long articleId, Long lastCommentId, Integer pageSize |

Response

```json
{
    "status" : 200,
    "message": "댓글 조회에 성공했습니다.",
    "data":{
        "commentList":[
            {
                "commentId":1,
                "content":"댓글 내용",
                "writerId":1,
                "writerName":"jayho",
                "writerProfileImage":"image.png",
                "articleId":1,
            },
            ...
        ]
    }
}
```

```json
// 실패
{
  "status": 404,
  "message": "게시글을 찾을 수 없습니다."
}
```



### 댓글 수정

| 항목   | 설명                |
| ------ | ------------------- |
| 메소드 | PATCH               |
| URL    | comment/{commentId} |
| 설명   | 댓글 수정           |
|        |                     |

Request

```json
{
    "articleId":1,
    "content":"수정한 댓글 내용",
    "wirterId":1
}
```

Response

```json
{
    "status" : 200,
    "message": "댓글 수정에 성공했습니다.",
}
```

```json
// 실패
{
  "status": 404,
  "message": "해당 댓글을 찾을 수 없습니다."
}
```



### 댓글 삭제

| 항목   | 설명                 |
| ------ | -------------------- |
| 메소드 | DELETE               |
| URL    | comments/{commentId} |
| 설명   | 작성자 댓글 삭제     |

Request

```json
{
    "articleId":1,
    "wirterId":1
}
```

Response

```json
{
    "status" : 200,
    "message": "댓글 삭제가 성공했습니다.",
}
```

```json
// 실패
{
  "status": 404,
  "message": "해당 댓글을 찾을 수 없습니다."
}
```



## 조회수

### 조회수 증가

| 항목   | 설명                                         |
| ------ | -------------------------------------------- |
| 메소드 | POST                                         |
| URL    | view-count/article/{articleId}/user/{userId} |
| 설명   | 해당 article 조회수 증가                     |

Response

```json
{
  "status" : 200,
  "message": "조회수 증가가 성공했습니다.",
}
```

```json
// 실패
{
  "status": 404,
  "message": "해당 게시글을 찾을 수 없습니다."
}
```



### 조회수 조회

| 항목   | 설명                           |
| ------ | ------------------------------ |
| 메소드 | GET                            |
| URL    | view-count/article/{articleId} |
| 설명   | 해당 article 조회수 증가       |

Response

```json
{
  "status" : 200,
  "message": "해당 게시글의 조회수가 조회되었습니다.",
}
```

```json
// 실패
{
  "status": 404,
  "message": "해당 게시글을 찾을 수 없습니다."
}
```





## ai-chat

### 대화 조회

| 항목   | 설명                                    |
| ------ | --------------------------------------- |
| 메소드 | GET                                     |
| URL    | ai-chat/user/{userId}/create/{createAt} |
| 설명   | AI 채팅에 대한 추천                     |

Response

```json
{
  "status" : 200,
  "message": "이전 대화 이력이 조회되었습니다.",
  "data":{
    "chat":
    [
      {
        "chatting":"0:ai답변/1:user질문",
        "timestamp":"20250619000000",
        "chattingLength":1234
      },
      ....
    ]
  }
}
```

```json
// 실패
{
  "status": 400,
  "message": "대화 이력을 조회하는데 실패하였습니다."
}
```



### 대화 생성

| 항목   | 설명                  |
| ------ | --------------------- |
| 메소드 | POST                  |
| URL    | ai-chat/user/{userId} |
| 설명   | AI 채팅 질문          |
|        |                       |

Request

```json
{
  "userId":1,
  "query":"대화를 생성할 수 있니?",
  "timestamp":"20250619000000"
}
```

Response

```json
{
  "status" : 200,
  "message": "이전 대화 이력이 조회되었습니다.",
  "data":{
    "chat":"ai답변"
  }
}
```

```json
// 실패
{
  "status": 400,
  "message": "대화 생성에 실패하였습니다."
}
```

