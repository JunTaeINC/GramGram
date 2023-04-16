# 🔎 2Week_KimJunTae.md

## 😄 Title: [2Week] 김준태

## 📕 미션 요구사항 분석 & 체크리스트
### 🔥 필수 미션
> 호감표시 할 때 예외처리 케이스 3가지 처리
> - 테스트 코드를 활용해서 TDD 방식으로 풀이
- [X] Case 1 : 중복으로 호감표시시 예외처리
  - InstaId ABCD -> abcd (외모)
  - InstaId ABCD -> abcd (외모) ❗예외 발생❗`rq.historyBack`를 활용
- [X] Case 2 : 11명 이상의 호감상대를 등록시 예외처리
  - `rq.historyBack`를 활용
- [X] Case 3 : 호감표시 사유 수정 기능 구현
  - InstaId ABCD -> abcd (외모)
    - abcd (외모)
  - InstaId ABCD -> abcd (성격)
    - abcd (성격) 으로 수정
    - `msg=bbbb 에 대한 호감사유를 외모에서 성격으로 변경합니다.` 메시지 출력
---
### 🔥 추가 미션
- [X] 네이버 로그인 기능구현
    - `NAVER__{"id": 2731659195, "gender": "M", "name": "홍길동"}` ❌
    - `NAVER__2731659195` ⭕️
- [X] 아이디 찾기
- [ ] 비밀번호 찾기
  - 임시 비밀번호 생성하여 이메일로 발송
- [X] 데이지 UI를 활용하여 디자인 개선
- [ ] QueryDSL 도입 (호감표시 상대 검색기능)
- [X] 회원가입시 축하메일 발송
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
> 💡 Case 1
- `rq.historyBack()`은 테스트코드에서 `redirectedUrlPattern`을 지원하지 않는다. 또한 원래 HTTP CODE 200을 반환하지만, 
 `resp.setStatus(HttpServletResponse.SC_BAD_REQUEST)`에서 따로 설정을 해주었기 떄문에 HTTP CODE 400을 반환.
  - `rq.redirectWithMsg()`는 HTTP CODE 300을 반환.
- JPA 메서드 명명규칙에의해 `Repository`에서의 메서드명 중요.

> 💡 Case 2
- `member.getInstaMember().getFromLikeablePeople().size() > 10` 인줄 알았지만 `member.getInstaMember().getFromLikeablePeople().size() > 9`
가 10명에서 추가 입력시 에러메세지 출력.
- `LikeablePersonController`에서 구현시 10명째 등록할때 에러메세지 오류코드 F로 `호감상대가 등록되었습니다.` 출력되서 `Service`에서 구현

> 💡 Case 3

- `@Builder`가 `LikeablePerson`엔티티에 적용되었지만, `@Setter`를 추가해서 매력포인트를 다시 설정 하는 기능을 구현했다.
`@Setter`를 구현 하지말고 `@Builder`를 구현 하는 것보다 코드가 간결하기 때문에 활용했는데 잘한건지 잘 모르겠다.
- Case 1 에서 만들었던 코드를 합칠 수 있을것 같다. 커밋을 하고 난후 리펙토링 과정에서 코드를 합칠 예정이다.

> 💡 네이버 로그인
- 네이버 로그인 같은경우 Json 그 안에서의 `name`값을 가져오는데에 시간이 오래 걸렸다. `get("response");`를 활용하는건 알고 있었는데 어디서 값을 가져오는지 
몰라서 시간이 걸렸다.
- 그렇게 다양한 방법으로 시도중에 `oauthId = (String) oAuth2User.getAttributes().get("name");`이렇게 작성하니 구글아이디가 나왔다. 우연치 않게 
발견하게 되었지만 이것 또한 활용하였다. `providerTypeCode`를 통해 어디 소셜로그인 인지 파악고 각자의 `oauthId`를 적용시켜줬다.

### **[🤔회고]**
- Case 1, Case 3 검증이 잘되는 줄 알았지만, 전체 `LikeablePerson`에서의 검증 때문에 이 부분에서 에러가 났다.
> - user1 -> likeLion(외모) ⭕️
> - user2 -> likeLion(능력) ❌ 에러 발생
- 그래서 코드를 수정했다. JPA 메서드를 활용하였는데
```java
# LikeablePersonRepository
Optional<LikeablePerson> findByFromInstaMemberAndToInstaMember_username(InstaMember instaMember,String username);

# LikeablePersonService
Optional<LikeablePerson> optionalLikeablePerson =
                likeablePersonRepository.findByFromInstaMemberAndToInstaMember_username(fromInstaMember, username);
```
- `findByFromInstaMember` -> `LikeablePerson` 중에서 `FromInstaMember`가 매개변수`fromInstaMember(InstaMember)` 이고(`And`),
- `ToInstaMember_username` -> `LikeablePerson` 중에서 `ToInstaMember`의 `UserName`이 매개변수`username` 인 객체로 활용했다.
- `And`의 갯수 + 1 = `매개변수`의 갯수 이다.
### **[💫리펙토링]**
- `LikeablePersonService.delete()`에서 검증하는 로직도 같이 들어있었다. 하지만 강사님의 코드를 보니 따로 `canDelete()`를 구현하여 검증메소드를 따로 만들어
주었다. 메소드는 한가지 일만 만들어야 한다. 그래서 나도 그렇게 분리를 해주었다.
- `LikeablePersonService.like()` 메소드에 검증과 수정,저장 기능을 같이 넣어서 구현을 했었는데 이 메소드 또한 분리를 해주었다.
  - 검증 : `canLike()`
  - 수정 : `modifyAttractiveTypeCode()`
  - 저장 : `like()`
- `Member`클래스의 매개변수 이름을 `member`로 작성하였는데 강사님이 피하라고 하셨다. 그래서 활동자 즉 `actor`로 매개변수 이름을 변경하였다.