# 탐정의 밤 API

## 도메인 설계
### 회원

- 회원 ID
- 닉네임
- 아이디
- 비밀번호
- 회원 활성화 상태 (true or false)
- 이메일

### 게시글

- 게시글 ID
- 작성자
- 작성날짜
- 제목
- 내용
- 게시글 타입(공지사항, 자유게시판, 추리 문제 등)
- 조회 수
- 추천 수
- 정답(추리문제일 경우)
- 해설

### 댓글

- 작성자
- 내용
- 작성 날짜
- 추천 수
- 대댓글(댓글이 댓글을 가지도록 계층적 구현)

- ### 사용 기술

---

- react
- spring, spring boot
- mysql
- SSE(Server-Sent Events, 단방향 알림 구현)
- AWS s3 (게시글 이미지 저장)
- GCP VM(가상머신), SQL(DB 서버)
- nginx

## erd
![image](https://github.com/WAME-LEL/find_me_mysteryweb/assets/56767018/9144e8c2-24d1-47ed-8f99-6a7e416267a8)
