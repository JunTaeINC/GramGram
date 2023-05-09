# 🔎 4Week_KimJunTae.md

## 😄 Title: [4Week] 김준태

## 📕 미션 요구사항 분석 & 체크리스트

### 🔥 필수 미션
- [X] `toList.html` 성별 필터링 기능 구현
  - [X] 테스터 코드 작성
- [ ] 네이버 클라우드 플랫폼을 통해 재배포
  - 배포, 도메인, HTTPS 까지 적용성
---

### 🔥 추가 미션
- [X] `toList.html` 호감사유 필터링 기능 구현
  - [X] 테스터 코드 작성
- [X] `toList.html` 정렬 기능 구현
  - [X] 테스터 코드 작성
  - [X] 날짜순
  - [X] 최신순(기본)
  - [X] 인기 많은 순
  - [X] 인기 적은 순
  - [X] 성별순
    - 1순위 : 여성 -> 남성 순
    - 2순위 : 최신순
  - [X] 호감사유순
    - 1순위 : 외모 -> 성격 -> 능력 순
    - 2순위 : 최신순
- [ ] 젠킨스를 통해서 리포지터리의 main 브랜치에 커밋 이벤트가 발생하면 자동으로 배포
---

### **[ 📗 접근 방법 ]**


### **[❗️특이사항❗️]**
- `toList.html` 성별 필터링 기능 구현
  - `gender`의 값을 가져오기 위해서 `@RequestParam String gender`를 사용하였다. `default` 값이 `required = true` 이기 때문에 파라미터 값이
  누락이 되면 `400 Bad Request`가 발생한다. 그래서 `@RequestParam(required = false) String gender`로 바꿨더니 이젠 `500 NullPointerException`
  에러가 발생했다. `gender`의 값이 `null`이 되서 생긴듯 하다. `if (gender == null)`를 추가해줘서 성공적으로 기능은 구현이 되었다. 하지만 리펙토링이 필요해보인다.
  - test 코드 작성시 `LikeablePersonRepository`를 활용해서 리스트의 사이즈를 구했지만 이것은 옳바른 방법이 아니였다. `gender=W` 였을때의 리스트의 사이즈를
  구하기 위해서는 `MvcResult`를 통해 응답받은 `html`을 `Jsoup 라이브러리`를 통해 파해서 코드를 구현할수 있었다.
- `toList.html` 호감사유 필터링 기능 구현
  - 성별 필터링 기능 구현을 기반으로 작성하니 쉽게 작성했다. 물론 리펙토링이 필요하다. 모든 기능을 다 구현을 한후 리펙토링을 할 계획이다. 이번에 호감사유별 테스트 코드를
  작성했지만 이전에 작업했더 성별 필터링 테스트 코드에서 호감사유까지 적어줘야지 기능이 작동했다.
- `toList.html` 정렬 기능 구현
  - 테스트 코드를 작성하는 중에 어떻게 첫번째 게시물을 가져올까를 고민을 하다가 위의 방법처럼 게시물에서 번호를 메기는 방법을 떠올렸다. 그렇게해서 GPT를 활용해서 구현을 해보았지만
  `<script>`는 화면에서 보여지는 값이므로 변화하지 않았다. 그래서 타임리프의 `th:each="likeablePerson, loop: ${likeablePeople}"` 를 활용해
  `<div th:attr="count=${loop.count}">` 를 통해서 값을 넣는데 성공했다. 하지만 이런방법을 활용하지는 못했다. 다른 방법인 `MvcResult`를 사용했다.
  `result.getModelAndView().getModel().get("likeablePeople");` 이렇게 해서 `List<LikeablePerson>`을 가져올수 있게 되었다.
  - 최신순으로 정렬이 기본이고, 나중에 2순위가 최신순이기 때문에 `switch`문 밖에서 최신순으로 정렬을 하였다. 성별순 으로 정렬을 할때 
  `.sorted(Comparator.comparing(e -> e.getFromInstaMember().getGender().equals("W")).reversed())` 이렇게 구현을 했더니 에러가 발생했다. 내용은
  `람다식에서 e는 어떤 객체든 될 수 있기 때문에 이 객체에 getFromInstaMember() 메서드가 존재하는지 컴파일러가 알 수 없습니다. 그래서 컴파일러는 e를 Object로 추론했을 가능성이 있습니다.`
  그래서 `e -> ((LikeablePerson)e)` 로 변경해주었다.
### **[🤔회고]**

### **[💫리펙토링]**
