# 22-5-team4-server

<div align="center">
  <h1>🧇 WaffleTicket 🎫 (Interpark Ticket clone)</h1> 
</div>

> 이 프로젝트는 **Spring Boot**를 사용한 **RESTful API 서버**이며,  
> 주요 기능은 **공연 검색 및 예매**, **게시글 CRUD** 등입니다.  
> 와플티켓 안드로이드 앱의 백엔드 서버 역할을 합니다.

[와플티켓 안드로이드 앱 바로가기](https://github.com/wafflestudio/22-5-team4-android)


## 목차

1. [프로젝트 개요](#프로젝트-개요-project-overview)
2. [기술 스택](#기술-스택-tech-stack)
3. [주요 기능](#주요-기능-features)
4. [설치 및 실행](#설치-및-실행-getting-started)
5. [환경 변수 / 설정](#환경-변수--설정-environment-variables)
6. [DB 구조](#db-구조-database-schema)
7. [API 명세](#api-명세-api-documentation)
8. [배포](#배포-deployment)
9. [기여](#기여-contributing)

---

## 프로젝트 개요 (Project Overview)

**와플티켓 안드로이드 앱 백엔드 서버**는 공연 정보 조회 및 등록, 리뷰 작성, 티켓 예매/취소 기능을 제공합니다.
- **목적**: 오프라인 공연의 티켓 예매 과정을 온라인 서비스로 전환
- **주요 특징**: 공연 일정/좌석/가격 관리, 예매/결제/취소, 리뷰 작성, 소셜 로그인 등
- **추가 내용**: 관리자와 일반 사용자의 접근 권한을 구분하며, 앱에서 직접 공연을 추가할 수 있도록 합니다


## 기술 스택 (Tech Stack)

- **언어(Language)**: <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white" style="vertical-align: middle;"> <img src="https://img.shields.io/badge/Scala-DC322F?logo=scala&logoColor=fff&style=flat" style="vertical-align: middle;">
- **프레임워크(Framework)**: <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white" style="vertical-align: middle;">
- **DB(Database)**: <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white" style="vertical-align: middle;">
- **빌드/의존성 관리**:  <img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=gradle&logoColor=white" style="vertical-align: middle;">
- **인증(Authentication)**: JWT (<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white" style="vertical-align: middle;">), OAuth 2.0(<img src="https://img.shields.io/badge/kakao-FFCD00?style=flat-square&logo=kakao&logoColor=black" style="vertical-align: middle;"> <img src="https://img.shields.io/badge/NAVER-03C75A?style=flat-square&logo=naver&logoColor=white" style="vertical-align: middle;">)
- **부하 테스트**: <img src="https://img.shields.io/badge/Gatling-FF9E2A?logo=gatling&logoColor=fff&style=flat" style="vertical-align: middles;">
- **기타**:
    - Docker & Docker Compose <img src="https://img.shields.io/badge/docker-2496ED?style=flat-square&logo=docker&logoColor=white" style="vertical-align: middle;">
    - Swagger (Springdoc) for API 문서화 <img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=swagger&logoColor=black" style="vertical-align: middle;">
    - AWS (EC2, RDS) 배포 가능 <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=flat-square&logo=amazon ec2&logoColor=white" style="vertical-align: middle;"> <img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=flat-square&logo=Amazon rds&logoColor=white" style="vertical-align: middle;">

- **도입 중**
    - Web proxy: <img src="https://img.shields.io/badge/NGINX-009639?logo=nginx&logoColor=fff&style=flat" style="vertical-align: middle;">
    - MessageQueue: <img src="https://img.shields.io/badge/RabbitMQ-F60?logo=rabbitmq&logoColor=fff&style=flat" style="vertical-align: middle;">

- **적용 검토 중**
    - SonarLint: ![SonarLint Badge](https://img.shields.io/badge/SonarLint-CB2029?logo=sonarlint&logoColor=fff&style=flat)
    - Orchestration: ![Kubernetes Badge](https://img.shields.io/badge/Kubernetes-326CE5?logo=kubernetes&logoColor=fff&style=flat)
    - GitOps: ![ArgoCD Badge](https://img.shields.io/badge/ArgoCD-EF7B4D?logo=argo&logoColor=fff&style=flat)
    - MSA:
        - ![Phoenix Framework Badge](https://img.shields.io/badge/Phoenix%20Framework-FD4F00?logo=phoenixframework&logoColor=fff&style=flat)
        - ![Actix Badge](https://img.shields.io/badge/Actix-000?logo=actix&logoColor=fff&style=flat)

| 구분         | 기술                           | 비고                                   |
|--------------|--------------------------------|--------------------------------------|
| Backend      | Spring Boot 3 (Kotlin)         | Java 23 기반                            |
| DB           | MySQL 8                        | Docker Compose로 컨테이너 실행 가능           |
| Auth         | JWT, Social(OAuth2)            | Access/Refresh Token 발급, 카카오/네이버 로그인 |
| Infra        | AWS (EC2, RDS), Docker         | 개발/테스트/운영 환경 분리                      |


## 주요 기능 (Features)

1. **공연**
    - 관리자 전용: 공연 등록/수정/삭제 (제목, 상세 정보, 일정, 포스터 등)
    - 카테고리별 공연 목록 조회
    - 공연 검색 기능
2. **티켓 예매/취소**
    - 공연 좌석 조회, 특정 좌석 예매 진행
    - 예매 취소 시 환불 로직 등
3. **회원가입 / 로그인**
    - **로컬 로그인**: 아이디/비밀번호
    - **소셜 로그인**: 카카오, 네이버 등의 OAuth2 인증
    - JWT 토큰 발급, 재발급(리프레시 토큰)
4. **마이페이지**
    - 예매 내역 조회, 예매 취소
5. **캐싱 / 성능 최적화**
    - 공연 목록, 좌석 정보 캐싱
    - 대규모 트래픽 대비 확장성 확보
## 주요 기능 구현 방식
### - 액세스 토큰 발급, 재발급
기본적인 유저 인증은 Jwt를 이용해 처리했습니다

accessToken의 만료시간은 15분으로 짧게 두고, refreshToken을 사용해 재발급할 수 있도록 하였습니다.
이를 통해 토큰 탈취의 위험성을 줄이고자 했습니다.
refreshToken은 자동으로 쿠키로 전달됩니다.

### - 소셜 로그인
안드로이드 클라이언트에서 **OAuth2 소셜 로그인**을 수행하면, 인증 서버(카카오, 네이버 등)로부터 **액세스 토큰**을 발급받습니다.  
이후, 클라이언트는 해당 액세스 토큰을 백엔드 서버로 전달하며, 서버는 이를 활용하여 소셜 인증 서버에서 **사용자 정보를 조회**합니다.

조회한 사용자 정보는 기존 로컬 계정과의 연동 여부를 확인하는 데 사용되며, **연동 여부**에 따라 다음과 같은 처리 과정이 진행됩니다.
- 연동된 계정 존재 → 기존 사용자로 로그인 처리 및 응답 반환
- 연동된 계정 없음 → 404 에러 반환 (연동 가능하도록 추가 정보 제공) → `/api/v1/social/link`을 통해 연동 요청

### - 페이지네이션 구현
무한스크롤을 구현하기 위한 페이지네이션을 구현했습니다

no offset 방식으로 구현을 했고, 이를 위해 마지막으로 반환한 정보를 나타내는 엔티티를 가리키는 cursor를 사용했습니다

cursor는 id와 정렬기준값을 기반으로 서버에서 생성하였습니다. CursorPageService를 여러 서비스 로직에서 범용적으로 사용할 수 있도록 하기 위해 신경썼습니다.
```aiignore
/api/v2/performance/search
/api/v2/performance/{performanceId}/review
/api/v2/review/{reviewId}/reply
```
위 서비스들에 페이지네이션을 적용했습니다

### - 좌석 & 예매 구현
공연의 좌석들을 조회하고, 해당 공연의 예매 정보를 조회하여 아직 예매되지 않은 예매 가능한 좌석을 반환하도록 만들었습니다.

예매정보를 저장하는 db에서 같은 좌석의 데이터가 들어올 수 없도록 uniqueConstraints를 설정했습니다 .

같은 좌석에 여러 예매 요청이 들어왔을 때 하나만 통과하는 것을 테스트로 확인했습니다.


### - 좌석 & 예매 최적화 (origin/add-redis-cache)
공연 좌석을 예약할 때 unique한 키로 redis에 락을 걸게 됩니다.  
삭제될 때는 동일한 키로 락을 해제합니다.
- 동시성 문제를 해결합니다. 다른 예약 로직이 돌아가고 있을 때 다른 서버에서 동일한 요청을 처리하지 않습니다.  

공연 좌석 정보를 전달하는 메서드에 lookaside 캐싱을 추가했습니다.
- GET 요청이고, 남은 좌석 전부를 돌려주는 것이므로 캐싱이 가능합니다.
- 예매/취소가 성공하면 CacheEvict를 통해 캐시를 무효화합니다.

### - Gatling으로 부하테스트 수행
Gatling을 통해 1k 연결 / 100건의 반복 예매 요청으로 테스트했습니다.  
- 가장 대기시간이 긴 것은 회원가입과 로그인입니다.
    - Jwt에서 작동하는 해시 검증이 부하를 주고 있었습니다.
    - 해당 기능은 rate limit을 통해 DoS를 막을 예정입니다.
- 공연 좌석 정보를 전달하는 메서드의 대기시간이 50%까지 감소한 것을 확인했습니다.


### (개발 중) NGINX -> RabbitMQ 트래픽 리다이렉션
Gatling 부하 테스트에서 connection이 많아지면 성능이 급격하게 떨어지는 점을 확인했습니다.  
따라서 cache로 빠르게 처리될 수 없는 예매 요청을 RabbitMQ로 보내고, 기존의 컨트롤러에서 처리하던 예매 기능은 RabbitMQ로부터 요청을 가져와 반환하는 형식으로 변경 중입니다.
- 이 경우 RabbitMQ에서 메시지를 받아와 처리하기만 하면 되므로, MSA를 적용하기 매우 편리합니다.
- 전체 서버는 매우 무겁습니다 - 예매요청 처리를 위해서는 매우 가벼운 pod들만 배포해도 문제없습니다.
    - MSA 구조를 이용하면 저비용으로도 높은 순간 트래픽을 견딜 수 있을 것입니다.

### (개발 중) SonarLint
UserIdentityEntity와 같은 민감 객체들에 대한 접근 통제를 위해 SonarLint를 적용할 예정입니다.  

## 설치 및 실행 (Getting Started)

### 사전 요구사항 (Prerequisites)

- **Java 17** 이상 / Kotlin
- **Gradle 7.x**
- **MySQL 8.x** (혹은 Docker로 MySQL 실행)
- **Git** (프로젝트 클론)

### 설치 / 실행 (Installation / Run)

1. **레포지토리 클론**
    ```bash
    git clone https://github.com/wafflestudio/22-5-team4-server.git
    cd 22-5-team4-server
    ```

2. **환경 변수 설정**
    - `.env` 파일 또는 `application.yml`에 DB, JWT 시크릿, 소셜 로그인 client-id 등 설정
    - 자세한 사항은 [환경 변수 / 설정](#환경-변수--설정-environment-variables) 참고

3. **빌드 및 로컬 배포**
    ```bash
    make
    ``` 
   make 명령어 실행 시 빌드와 docker를 이용한 배포가 같이 이루어집니다

   접속 도메인: `http://localhost`
## 환경 변수 / 설정 (Environment Variables)
로컬 배포시 .env 파일을 아래와 같이 설정합니다
```aiignore
SPRING_DATASOURCE_URL: "jdbc:mysql://mysql-db:3306/testdb"
SPRING_DATASOURCE_USERNAME: "user"
SPRING_DATASOURCE_PASSWORD: "somepassword"
JWT_SECRET_KEY: "???"
KAKAO_CLIENT_ID: ???
KAKAO_CLIENT_SECRET: ???
NAVER_CLIENT_ID: ???
NAVER_CLIENT_SECRET: ???
```
JWT_SECRET_KEY는 32자 이상의 적당한 문자열을 사용하면 됩니다


## DB 구조 (Database Schema)


### 엔티티 구조
![EntityRelationDiagram.png](EntityRelationDiagram.png)


## API 명세
### [와플 티켓 벡엔드 서버 API 문서](http://15.164.225.121/swagger-ui/index.html#/)  
위 링크가 만료되었거나 접근이 불가능한 경우, 프로젝트를 직접 빌드 및 실행한 후 [링크](http://localhost/swagger-ui/index.html)에서 API 문서를 확인할 수 있습니다.


## 배포 (deployment)


## 기여 (contributing)

- [@Grantzile](https://github.com/Grantzile)
- [@ChungPlusPlus](https://github.com/ChungPlusPlus)
- [@kdh8156](https://github.com/kdh8156)