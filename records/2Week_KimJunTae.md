# 🔎 2Week_KimJunTae.md

## 😄 Title: [2Week] 김준태

## 📕 미션 요구사항 분석 & 체크리스트
### 🔥 필수 미션
> 호감표시 할 때 예외처리 케이스 3가지 처리
> - 테스트 코드를 활용해서 TDD 방식으로 풀이
- [X] Case 1 : 중복으로 호감표시시 예외처리
  - InstaId ABCD -> abcd (외모)
  - InstaId ABCD -> abcd (외모) ❗예외 발생❗`rq.historyBack`를 활용
- [ ] Case 2 : 11명 이상의 호감상대를 등록시 예외처리
  - `rq.historyBack`를 활용
- [ ] Case 3 : 호감표시 사유 수정 기능 구현
  - InstaId ABCD -> abcd (외모)
    - abcd (외모)
  - InstaId ABCD -> abcd (성격)
    - abcd (성격) 으로 수정
    - `msg=bbbb 에 대한 호감사유를 외모에서 성격으로 변경합니다.` 메시지 출력
---
### 🔥 추가 미션
- [ ] 네이버 로그인 기능구현
    - `NAVER__{"id": 2731659195, "gender": "M", "name": "홍길동"}` ❌
    - `NAVER__2731659195` ⭕️
- [ ] 아이디 찾기
- [ ] 비밀번호 찾기
  - 임시 비밀번호 생성하여 이메일로 발송
- [ ] 데이지 UI를 활용하여 디자인 개선
- [ ] QueryDSL 도입 (호감표시 상대 검색기능)
- [ ] 회원가입시 축하메일 발송
- [ ] 회원가입시 프로필 이미지 업로드
- [ ] 소셜 로그인시 프로필이미지 가져오기
- [ ] 누군가가 본인에게 호감표시를 했을 때 알림
- [ ] 기존코드 리펙토링
---

## 📗 2주차 미션 요약

---

### **[ 📎 접근 방법 ]**

> Case 1 : 중복으로 호감표시시 예외처리
> - `LikeablePersonService`에 `like()` 메서드를 활용해서 해결
> - 만약 추가하려는 호감상대가 있을시 `rq.historyBack`

> Case 2 : 11명 이상의 호감상대를 등록시 예외처리
> - `LikeablePersonService`에 `like()` 메서드를 활용해서 해결
> - `fromLikeablePeople.size() > 10` 예외 발생

> Case 3 : 호감표시 사유 수정 기능 구현
> - `LikeablePersonService`에 `like()` 메서드를 활용해서 해결
> - 만약 입력한 호감상대가 있을시 `set` or `build`로 정보 변경

### **[❗️특이사항❗️]**

- `rq.historyBack()`은 테스트코드에서 `redirectedUrlPattern`을 지원하지 않는다. 또한 원래 HTTP CODE 200을 반환하지만, 
 `resp.setStatus(HttpServletResponse.SC_BAD_REQUEST)`에서 따로 설정을 해주었기 떄문에 HTTP CODE 400을 반환.
  - `rq.redirectWithMsg()`는 HTTP CODE 300을 반환.
- JPA 메서드 명명규칙에의해 `Repository`에서의 메서드명 중요.


### **[🤔회고]**

### **[💫리펙토링]**