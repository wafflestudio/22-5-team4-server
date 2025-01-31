# 22-5-team4-server

# 🧇 WaffleTicket 🎫 (Interpark Ticket clone)


> 이 프로젝트는 **Spring Boot**를 사용한 **RESTful API 서버**이며,  
> 주요 기능은 **사용자 인증/인가(JWT)**, **게시글 CRUD** 등입니다.  
> 와플티켓 안드로이드 앱의 백엔드 서버 역할을 합니다.
---

## 목차

1. [프로젝트 개요](#프로젝트-개요-project-overview)
2. [기술 스택](#기술-스택-tech-stack)
3. [주요 기능](#주요-기능-features)
4. [설치 및 실행](#설치-및-실행-getting-started)
5. [환경 변수 / 설정](#환경-변수--설정-environment-variables)
6. [DB 구조](#db-구조-database-schema)
7. [API 명세](#api-명세-api-documentation)
8. [테스트](#테스트-testing)
9. [배포](#배포-deployment)
10. [라이선스](#라이선스-license)
11. [기여](#기여-contributing)

---

## 프로젝트 개요 (Project Overview)

**와플티켓 안드로이드 앱 백엔드 서버**는 공연 정보를 등록하고, 사용자가 티켓을 예매/취소할 수 있는 기능을 제공합니다.
- **목적**: 오프라인 공연의 티켓 예매 과정을 온라인 서비스로 전환
- **주요 특징**: 공연 일정/좌석/가격 관리, 예매/결제/취소, 소셜 로그인 등
- **추가 내용**: 관리자와 일반 사용자의 접근 권한을 구분하며, 포스터/백드롭 이미지 등 공연 상세 정보를 다룹니다.

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

1. **공연 관리**
    - 관리자 전용: 공연 등록/수정/삭제 (제목, 상세 정보, 일정, 가격, 포스터, 백드롭 이미지 등)
    - 카테고리별 공연 목록 조회
2. **티켓 예매/취소**
    - 공연 좌석 조회, 특정 좌석 예매 진행
    - 결제(단순 시뮬레이션 혹은 결제 모듈 연동)
    - 예매 취소 시 환불 로직 등
3. **회원가입 / 로그인**
    - **로컬 로그인**: 아이디/비밀번호
    - **소셜 로그인**: 카카오, 네이버 등의 OAuth2 인증
    - JWT 토큰 발급, 재발급(리프레시 토큰)
4. **마이페이지**
    - 예매 내역, 예매 취소 내역 조회
    - 공연 찜/즐겨찾기 (선택사항)
5. **관리자 기능**
    - 유저 목록 조회(권한 관리)
    - 공연 관련 통계, 매출 리포트(선택사항)
6. **캐싱 / 성능 최적화** (선택 사항)
    - 공연 목록, 좌석 정보 캐싱
    - 대규모 트래픽 대비 확장성 확보

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
    git clone https://github.com/yourusername/ticketing-backend.git
    cd ticketing-backend
    ```

2. **환경 변수 설정**
    - `.env` 파일 또는 `application.yml`에 DB, JWT 시크릿, 소셜 로그인 client-id 등 설정
    - 자세한 사항은 [환경 변수 / 설정](#환경-변수--설정-environment-variables) 참고

3. **의존성 설치 & 빌드**
    ```bash
    ./gradlew clean build
    ```

4. **애플리케이션 실행**
    ```bash
    ./gradlew bootRun
    ```
    - 기본 포트: `http://localhost:8080`