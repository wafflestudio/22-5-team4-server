# 22-5-team4-server

# 🧇 WaffleTicket 🎫 (Interpark Ticket clone)


> 이 프로젝트는 **Spring Boot**를 사용한 **RESTful API 서버**이며,  
> 주요 기능은 **공연 검색 및 예매**, **게시글 CRUD** 등입니다.  
> 와플티켓 안드로이드 앱의 백엔드 서버 역할을 합니다.
---

## 목차

1. [프로젝트 개요](#프로젝트-개요-project-overview)
2. [기술 스택](#기술-스택-tech-stack)
3. [주요 기능](#주요-기능-features)
4. [설치 및 실행](#설치-및-실행-getting-started)
5. [환경 변수 / 설정](#환경-변수--설정)
6. [DB 구조](#DB-구조)
7. [API 명세](#API-명세)
8. [배포](#배포-deployment)
9. [라이선스](#라이선스-license)
10. [기여](#기여-contributing)

---

## 프로젝트 개요 (Project Overview)

**와플티켓 안드로이드 앱 백엔드 서버**는 공연 정보를 등록하고, 사용자가 티켓을 예매/취소할 수 있는 기능을 제공합니다.
- **목적**: 오프라인 공연의 티켓 예매 과정을 온라인 서비스로 전환
- **주요 특징**: 공연 일정/좌석/가격 관리, 예매/결제/취소, 소셜 로그인 등
- **추가 내용**: 관리자와 일반 사용자의 접근 권한을 구분하며, 앱에서 직접 공연을 추가할 수 있도록 합니다

---

## 기술 스택 (Tech Stack)

- **언어(Language)**: <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white" style="vertical-align: middle;">
- **프레임워크(Framework)**: <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white" style="vertical-align: middle;">
- **DB(Database)**: <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white" style="vertical-align: middle;">
- **빌드/의존성 관리**:  <img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=gradle&logoColor=white" style="vertical-align: middle;">
- **인증(Authentication)**: JWT (<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white" style="vertical-align: middle;">), OAuth 2.0(<img src="https://img.shields.io/badge/kakao-FFCD00?style=flat-square&logo=kakao&logoColor=black" style="vertical-align: middle;"> <img src="https://img.shields.io/badge/NAVER-03C75A?style=flat-square&logo=naver&logoColor=white" style="vertical-align: middle;">)
- **기타**:
    - Docker & Docker Compose <img src="https://img.shields.io/badge/docker-2496ED?style=flat-square&logo=docker&logoColor=white" style="vertical-align: middle;">
    - Swagger (Springdoc) for API 문서화 <img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=swagger&logoColor=black" style="vertical-align: middle;">
    - AWS (EC2, RDS) 배포 가능 <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=flat-square&logo=amazon ec2&logoColor=white" style="vertical-align: middle;"> <img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=flat-square&logo=Amazon rds&logoColor=white" style="vertical-align: middle;">

| 구분         | 기술                           | 비고                                   |
|--------------|--------------------------------|--------------------------------------|
| Backend      | Spring Boot 3 (Kotlin)         | Java 23 기반                            |
| DB           | MySQL 8                        | Docker Compose로 컨테이너 실행 가능           |
| Auth         | JWT, Social(OAuth2)            | Access/Refresh Token 발급, 카카오/네이버 로그인 |
| Infra        | AWS (EC2, RDS), Docker         | 개발/테스트/운영 환경 분리                      |

---

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
---
## 주요 기능 구현 방식
### 액세스 토큰 발급, 재발급
기본적인 유저 인증은 Jwt를 이용해 처리했습니다

accessToken의 만료시간은 15분으로 짧게 두고, refreshToken을 사용해 재발급할 수 있도록 하였습니다.
이를 통해 토큰 탈취의 위험성을 줄이고자 했습니다.
refreshToken은 자동으로 쿠키로 전달됩니다.

### 페이지네이션 구현
무한스크롤을 구현하기 위한 페이지네이션을 구현했습니다

no offset 방식으로 구현을 했고, 이를 위해 마지막으로 반환한 정보를 나타내는 엔티티를 가리키는 cursor를 사용했습니다

cursor는 id와 정렬기준값을 기반으로 서버에서 생성하였습니다. CursorPageService를 여러 서비스 로직에서 범용적으로 사용할 수 있도록 하기 위해 신경썼습니다.
```aiignore
/api/v2/performance/search
/api/v2/performance/{performanceId}/review
/api/v2/review/{reviewId}/reply
```
위 서비스들에 페이지네이션을 적용했습니다

### 좌석 & 예매 구현
공연의 좌석들을 조회하고, 해당 공연의 예매 정보를 조회하여 아직 예매되지 않은 예매 가능한 좌석을 반환하도록 만들었습니다.

예매정보를 저장하는 db에서 같은 좌석의 데이터가 들어올 수 없도록 uniqueConstraints를 설정했습니다 .

같은 좌석에 여러 예매 요청이 들어왔을 때 하나만 통과하는 것을 테스트로 확인했습니다.

---
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
## 환경 변수 / 설정
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

---
## DB 구조
### 엔티티 구조
![EntityRelationDiagram.png](EntityRelationDiagram.png)
## API 명세
로컬로 배포 후 `http://localhost/swagger-ui/index.html#/`
접속해서 확인 가능합니다
## 배포 deployment
## 라이선스 license
## 기여 contributing