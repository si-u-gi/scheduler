# 시대생 크루 스케줄러 & 자료 공유 플랫폼 - 프로젝트 명세서

## 1. 프로젝트 개요

**프로젝트명**: `scheduler-crew` (시대생 크루)  
**설명**: 동아리(크루) 회원 간 일정 관리, 과제 공유, 자료를 주고받을 수 있는 플랫폼  
**목표**: Spring Boot + PostgreSQL 기반 백엔드를 구축하고, 동아리에서 실제로 사용하는 시스템을 만듦

---

## 2. 기술 스택

### 백엔드
| 항목 | 선택 |
|------|------|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.2.x |
| **Build Tool** | Gradle (Kotlin DSL) |
| **DB** | PostgreSQL 15+ |
| **ORM** | Spring Data JPA (Hibernate) |
| **Security** | Spring Security + JWT |
| **Validation** | Jakarta Bean Validation |
| **API Docs** | SpringDoc OpenAPI 3 (Swagger) |
| **Lombok** | Lombok |

### 인프라 (Phase 3에서 적용)
| 항목 | 선택 |
|------|------|
| **CI/CD** | GitHub Actions |
| **Deploy** | Docker + Docker Compose |
| **Cloud** | AWS EC2 또는 Railway |

---

## 3. 주요 기능 (Phased)

### Phase 1 - 핵심 뼈대 (3~4주)
- [ ] **인증 시스템**
  - 회원가입 (email, password, nickname)
  - 로그인 (JWT Access Token + Refresh Token)
  - 회원 정보 조회/수정
- [ ] **크루 (동아리) 시스템**
  - 크루 생성
  - 크루 가입 (초대코드 기반)
  - 크루 목록 조회
  - 크루 회원 목록

### Phase 2 - 일정 & 자료 (5~7주)
- [ ] **캘린더 / 일정**
  - 일정 CRUD (제목, 내용, 날짜, 크루 연결)
  - 팀별 일정 조회
- [ ] **자료 공유**
  - 자료 업로드 (제목, 설명, 파일명, 태그)
  - 자료 목록 조회 (태그 검색, 크루 필터)
  - 자료 상세 조회 + 다운로드 카운트
  - 댓글 CRUD

### Phase 3 - 확장 (8~10주)
- [ ] **알림 시스템** (이메일 or in-app)
- [ ] **실시간 채팅** (WebSocket)
- [ ] **좋아요 / 북마크**

---

## 4. 데이터 모델 (ERD)

### Users (회원)
```
users
├── id (BIGSERIAL, PK)
├── email (VARCHAR, UNIQUE, NOT NULL)
├── password (VARCHAR, NOT NULL) — BCrypt 인코딩
├── nickname (VARCHAR, NOT NULL)
├── created_at (TIMESTAMP)
└── updated_at (TIMESTAMP)
```

### Crews (크루)
```
crews
├── id (BIGSERIAL, PK)
├── name (VARCHAR, NOT NULL)
├── description (TEXT)
├── invite_code (VARCHAR, UNIQUE) — 6자리 초대코드
├── created_at (TIMESTAMP)
└── creator_id (FK → users.id)
```

### Crew_Members (크루 회원)
```
crew_members
├── id (BIGSERIAL, PK)
├── crew_id (FK → crews.id)
├── user_id (FK → users.id)
├── role (VARCHAR) — 'LEADER', 'MEMBER'
├── joined_at (TIMESTAMP)
└── UNIQUE(crew_id, user_id)
```

### Schedules (일정)
```
schedules
├── id (BIGSERIAL, PK)
├── crew_id (FK → crews.id)
├── title (VARCHAR, NOT NULL)
├── description (TEXT)
├── start_time (TIMESTAMP)
├── end_time (TIMESTAMP)
├── created_at (TIMESTAMP)
└── creator_id (FK → users.id)
```

### Materials (자료)
```
materials
├── id (BIGSERIAL, PK)
├── crew_id (FK → crews.id)
├── title (VARCHAR, NOT NULL)
├── description (TEXT)
├── file_path (VARCHAR) — 서버 저장 경로
├── file_size (BIGINT)
├── download_count (INT, DEFAULT 0)
├── created_at (TIMESTAMP)
└── creator_id (FK → users.id)
```

### Tags (태그)
```
tags
├── id (BIGSERIAL, PK)
└── name (VARCHAR, UNIQUE)
```

### Material_Tags (자료-태그 관계)
```
material_tags
├── material_id (FK → materials.id)
└── tag_id (FK → tags.id)
```

### Comments (댓글)
```
comments
├── id (BIGSERIAL, PK)
├── material_id (FK → materials.id)
├── user_id (FK → users.id)
├── content (TEXT)
├── created_at (TIMESTAMP)
└── updated_at (TIMESTAMP)
```

### Refresh_Tokens
```
refresh_tokens
├── id (BIGSERIAL, PK)
├── user_id (FK → users.id)
├── token (VARCHAR, UNIQUE)
├── expires_at (TIMESTAMP)
└── created_at (TIMESTAMP)
```

---

## 5. API 설계 개요

### 인증
| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/v1/auth/signup` | 회원가입 |
| POST | `/api/v1/auth/login` | 로그인 |
| POST | `/api/v1/auth/refresh` | 토큰 갱신 |
| POST | `/api/v1/auth/logout` | 로그아웃 |

### 크루
| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/v1/crews` | 크루 생성 |
| GET | `/api/v1/crews` | 크루 목록 |
| POST | `/api/v1/crews/join` | 크루 가입 (초대코드) |
| GET | `/api/v1/crews/{id}` | 크루 상세 |
| GET | `/api/v1/crews/{id}/members` | 크루 회원 목록 |

### 일정
| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/v1/schedules` | 일정 생성 |
| GET | `/api/v1/schedules?crewId=` | 크루별 일정 조회 |
| GET | `/api/v1/schedules/{id}` | 일정 상세 |
| PUT | `/api/v1/schedules/{id}` | 일정 수정 |
| DELETE | `/api/v1/schedules/{id}` | 일정 삭제 |

### 자료
| Method | Endpoint | 설명 |
|--------|----------|------|
| POST | `/api/v1/materials` | 자료 등록 |
| GET | `/api/v1/materials?crewId=&tag=` | 자료 목록 조회 |
| GET | `/api/v1/materials/{id}` | 자료 상세 |
| DELETE | `/api/v1/materials/{id}` | 자료 삭제 |
| GET | `/api/v1/materials/{id}/download` | 자료 다운로드 |
| POST | `/api/v1/materials/{id}/comments` | 댓글 작성 |
| GET | `/api/v1/materials/{id}/comments` | 댓글 목록 |

---

## 6. 구현 체크리스트 (Phase 1 기준)

### Week 1-2: 프로젝트 세팅 + 인증
- [ ] Spring Boot 프로젝트 생성 (Gradle)
- [ ] PostgreSQL 연결 설정
- [ ] JPA Entity 설계 + ERD 문서화
- [ ] 회원가입 API + Validation
- [ ] 로그인 API + BCrypt
- [ ] JWT Access/Refresh Token 구현
- [ ] Security Config 설정
- [ ] API 문서화 (Swagger) 세팅

### Week 3-4: 크루 시스템
- [ ] 크루 생성 API
- [ ] 초대코드 생성 로직
- [ ] 크루 가입 API
- [ ] 크루 목록/상세/회원 목록 API
- [ ] 크루 역할 (LEADER/MEMBER) 설정
- [ ] 크루 삭제 API (LEADER만)
- [ ] Exception Handling 체계화

---

## 7. 배울 점 정리

|기술|배울 내용|
|------|---------|
| **PostgreSQL** | Oracle → PostgreSQL 차이 (시퀀스, TEXT 타입, 배열 등) |
| **JWT** | Access Token + Refresh Token 분리, 블랙리스트 |
| **Spring Security** | Filter Chain, CORS, Stateless 세션 |
| **REST API** | RESTful 설계, 응답 구조 표준화 |
| **TDD/Integration Test** | JUnit5, MockMvc, Testcontainers |
| **Git Branch** | feature/* 브랜치 전략 |

---

## 8. 레퍼런스

- Spring Boot 3.2 Docs: https://docs.spring.io/spring-boot/docs/3.2.x/reference/html/
- Spring Security: https://docs.spring.io/spring-security/reference/
- Spring Data JPA: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
- PostgreSQL Doc: https://www.postgresql.org/docs/
- JWT Library: jjwt (https://github.com/jwtk/jjwt)
