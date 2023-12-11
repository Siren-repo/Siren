## 🧩 Summary

---

스타벅스의 Siren Order 서비스를 간단하게 구현해보고, 분산환경으로 구성하여 안정적인 트래픽 처리를 할 수 있도록 구현하는 것을 목표로 프로젝트를 진행하고 있습니다

- 유저는 매장을 선택하여 원하는 아이템을 담고 주문을 진행할 수 있습니다
- 어드민은 매장과 아이템을 등록하고 재고를 관리합니다
- 스태프는 들어온 주문을 승인하고 주문의 상태를 변경 할 수 있습니다

[UseCase WIKI](https://github.com/Siren-repo/Siren/wiki/%F0%9F%91%A4-User-Sceanario)

[API Design WIKI](https://github.com/Siren-repo/Siren/wiki/%E2%9C%8D%EF%B8%8F-API-Design)

## 🏡  Team

---

| <img src="https://avatars.githubusercontent.com/u/35358294?v=4" width="130" height="130"> | <img src ="https://avatars.githubusercontent.com/u/98626972?v=4" width="130" height="130"> 
|:-----------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------:| 
|                          [yooyouny](https://github.com/yooyouny)                          |                            [Ho-Tea](https://github.com/Ho-Tea)                             

[Team WIKI](https://github.com/Siren-repo/Siren/wiki/%F0%9F%A4%9D-Convention)

[Convention WIKI](https://github.com/Siren-repo/Siren/wiki/%F0%9F%A4%9D-Convention)

## ⚙️  Tech Stack

---

Java 11, Spring Boot, Gradle, MySQL 8.0, Redis, Spring Data JPA, Spring Security, JWT, SSEmitter, Apache Kafka

junit5, mockito, H2 Database

Jenkins, Docker, Github Actions, AWS EC2

REST API Docs, JACOCO

## 🎨  Architecture (2023.12 ver)

---

### ERD

![img_4.png](.github/imgs/img_4.png)

### System Architecture

- 단일 서버 버전

  ![img_3.png](.github/imgs/img_3.png)

### CI/CD Pipeline

<img width="721" alt="image" src="https://github.com/Siren-repo/Siren/assets/35358294/10e8e0ae-b02f-47af-9f09-db358dae5881">

[배포전략 WIKI](https://github.com/Siren-repo/Siren/wiki/%E2%98%81%EF%B8%8F-Deploy-Strategy)

## 🎯 We Interested in

---

### Tech Issue

- 객체지향적인 코드 작성하기
- 레이어 별 독립적인 단위 테스트 코드 작성하기
- 테스트 커버리지 기준 달성하기
- 코드통합 시 오류 방지를 위한 CI, 배포 자동화를 위한 CD 구축하고 시작하기
- 외부 API 통신 시 비동기로 처리하여 성능 개선하기
- 인덱스 활용, 실행계획을 통한 쿼리 튜닝 실천하기
- Replication 적용으로 read 성능 높이기
- 기능 구현 완료 후 단일서버 → 분산서버 환경으로 Scale-out 해보기
- 성능테스트를 진행하여 병목지점 개선해보기
- 기술들의 trade-off에 대해서 고민하고 좋은 전략 선택하기

### Soft Skill

- 모든 이슈 해결과정은 문서화하기
- Github 협업 도구 적극 활용하기
- 가독성이 좋은 코드 지향하기

## 🔨 ISSUE

---

[#2](https://github.com/Siren-repo/Siren/issues/2) 코드통합/배포 안정적으로 진행하기
[Post](https://yooyouny.tistory.com/)

[#41](https://github.com/Siren-repo/Siren/issues/41) 주문 동시성 문제 처리하기

[#42](https://github.com/Siren-repo/Siren/issues/42) 인터셉터를 활용하여 권한체크로직 분리하기

*…and more*

## 🏃🏼‍ Sprint

---
[스프린트 정보](https://github.com/Siren-repo/Siren/milestones)

[프로젝트 백로그](https://github.com/orgs/Siren-repo/projects/1/views/1)