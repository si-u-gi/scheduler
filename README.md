# Scheduler - 동아리 일정 관리 플랫폼

> 동아리 일정 관리 및 자료 공유 플랫폼

## 🚀 기술 스택

- **Backend**: Java 17 + Spring Boot 3.2.5
- **Database**: PostgreSQL 15
- **Build Tool**: Gradle (Kotlin DSL)
- **Security**: Spring Security + JWT
- **API Docs**: SpringDoc OpenAPI (Swagger)
- **ORM**: Spring Data JPA (Hibernate)

## 📦 프로젝트 구조

```
scheduler/
├── SPEC.md                    ← 프로젝트 기술 명세서
├── PROJECT_PLAN.md            ← 프로젝트 계획서
├── README.md                  ← 이 파일
├── docker-compose.yml         ← PostgreSQL 실행용
├── build.gradle.kts           ← Gradle 빌드 설정
├── settings.gradle.kts
└── src/main/
    ├── resources/
    │   └── application.yml    ← 애플리케이션 설정
    └── java/com/scheduler/
        ├── SchedulerApplication.java
        ├── config/             ← Security, OpenAPI 설정
        ├── controller/         ← REST API Controller
        ├── dto/                ← Data Transfer Object
        │   └── request/        ← 요청 DTO
        ├── entity/             ← JPA Entity (8개)
        ├── exception/          ← 커스텀 Exception + Handler
        ├── repository/         ← JPA Repository (8개)
        ├── security/           ← JWT, Filter, UserDetails
        └── service/            ← Business Logic (6개)
```

## 🛠️ 설치 및 실행

### 1. prerequisites

- Java 17+
- PostgreSQL 15+
- Gradle (또는 ./gradlew 사용)

### 2. PostgreSQL 설정 (Docker)

```bash
# PostgreSQL 컨테이너 실행
docker-compose up -d
```

### 3. 애플리케이션 실행

```bash
# 빌드
./gradlew clean build

# 실행
./gradlew bootRun

# 또는 JAR 파일로 실행
java -jar build/libs/scheduler-0.0.1-SNAPSHOT.jar
```

### 4. API 문서 확인

- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs (JSON): http://localhost:8080/api-docs

## 📡 API Endpoints

### 인증 (Authentication)

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/v1/auth/signup` | 회원가입 |
| POST | `/api/v1/auth/login` | 로그인 |
| POST | `/api/v1/auth/refresh` | 토큰 갱신 |
| POST | `/api/v1/auth/logout` | 로그아웃 |

### 크루 (Crews)

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/v1/crews` | 크루 생성 |
| GET | `/api/v1/crews` | 내 크루 목록 |
| GET | `/api/v1/crews/{id}` | 크루 상세 |
| POST | `/api/v1/crews/join` | 크루 가입 |
| GET | `/api/v1/crews/{id}/members` | 크루 회원 목록 |
| DELETE | `/api/v1/crews/{id}` | 크루 삭제 |

### 일정 (Schedules)

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/v1/schedules` | 일정 생성 |
| GET | `/api/v1/schedules?crewId=` | 크루별 일정 조회 |
| GET | `/api/v1/schedules/{id}` | 일정 상세 |
| PUT | `/api/v1/schedules/{id}` | 일정 수정 |
| DELETE | `/api/v1/schedules/{id}` | 일정 삭제 |

### 자료 (Materials)

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/v1/materials` | 자료 등록 |
| GET | `/api/v1/materials?crewId=&tag=` | 자료 목록 |
| GET | `/api/v1/materials/{id}` | 자료 상세 |
| DELETE | `/api/v1/materials/{id}` | 자료 삭제 |
| GET | `/api/v1/materials/{id}/download` | 다운로드 카운트 |

### 댓글 (Comments)

| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/v1/materials/{id}/comments` | 댓글 작성 |
| GET | `/api/v1/materials/{id}/comments` | 댓글 목록 |
| PUT | `/api/v1/materials/{id}/comments/{cid}` | 댓글 수정 |
| DELETE | `/api/v1/materials/{id}/comments/{cid}` | 댓글 삭제 |

## 🔐 JWT 인증

모든 API (auth 제외)는 Bearer Token 인증이 필요합니다:

```
Authorization: Bearer <access_token>
```

## 🗄️ ERD (데이터 모델)

```
users (1) ─── (N) crew_members (N) ──── (1) crews
  │                                        │
  │                                        │
  └──── (N) schedules                      │
  │         │                              │
  │         │                              │
  └──── (N) materials ── (N) tags         │
              │                            │
              │                            │
           (N) comments ───────────────────┘
```

## 📅 개발 일정 (Phase 1 기준)

| 주 | 내용 |
|----|------|
| Week 1 | 프로젝트 세팅, PostgreSQL 연결, JPA Entity 설계 |
| Week 2 | 회원가입/로그인, JWT 구현 |
| Week 3 | 크루 시스템 (생성, 가입, 초대코드) |
| Week 4 | Exception Handling, API 문서화, 테스트 |

## 🎯 배울 점

1. **Oracle → PostgreSQL**: 시퀀스, TEXT 타입, 배열 등 차이점
2. **JWT Security**: Access/Refresh Token 분리
3. **REST API 설계**: 응답 구조 표준화
4. **Git Branch 전략**: feature/* 브랜치 관리
5. **실무 설계**: 예외처리, 유효성 검증, 계층 분리

---

**시대생 - 실력 있는 개발자를 모으자! 🚀**
