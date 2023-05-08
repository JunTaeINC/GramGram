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
- [ ] `toList.html` 호감사유 필터링 기능 구현
- [ ] `toList.html` 정렬 기능 구현
  - [ ] 날짜순
  - [ ] 최신순(기본)
  - [ ] 인기 많은 순
  - [ ] 인기 적은 순
  - [ ] 성별순
    - 1순위 : 여성 -> 남성 순
    - 2순위 : 최신순
  - [ ] 호감사유순
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

### **[🤔회고]**

### **[💫리펙토링]**
