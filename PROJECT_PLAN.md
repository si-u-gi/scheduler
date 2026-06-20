# 시대생 크루 스케줄러 - 프로젝트 계획서

> 동아리 일정 관리 및 자료 공유 플랫폼
> **개발 기간**: 2026.06.20 ~ 2026.08.31 (약 10주)

---

## 1. 프로젝트 개요

### 1.1背景

대학 동아리에서는 일정 공유, 과제 관리, 자료 배부를 위해 카카오톡 그룹채팅을 주로 사용하지만, 다음과 같은 문제가 있습니다:

- **일정 분실**: 중요한 일정이 채팅 속에埋没
- **자료散乱**: 과제, PPT, 참고자료가 채팅에 묻힘
- **알림 부재**: 다음 모임일정을 놓치기 쉬움
- **권한 없음**: 누구나 자료를 올릴 수 있어 관리가 어려움

### 1.2 プロジェクト名

**시대생 크루 (Sidaesaeng Crew)**

### 1.3 目标

동아리会员들이 일정과 자료를 체계적으로 管理できる プラットフォームを構築する

---

## 2. 機能要件

### 2.1 コア機能

| 優先度 | 機能 | 説明 |
|:---:|------|------|
| P0 | 認証システム |  회원가입, 로그인, JWT 토큰 |
| P0 | 크루 관리 |  크루 생성, 가입 (초대코드), 회원 목록 |
| P0 | 일정管理 |  CRUD, 팀별 캘린더 조회 |
| P1 | 資料共有 |  자료 업로드, 태그 검색, 다운로드 카운트 |
| P1 | コメント |  자료에 댓글 작성/조회 |
| P2 |  알림 |  이메일/Slack 연동 (Phase 3) |
| P2 |  チャット |  실시간 채팅 (Phase 3) |

### 2.2 非機能要件

- **性能**: API 응답시간 500ms 이하
- **セキュリティ**: JWT 기반 인증, BCrypt 비밀번호 해시
- ** Scalability**: Docker 컨테이너化为将来的に可能
- **運用**: Swagger API 문서 자동生成

---

## 3. 技術選定

### 3.1 技術スタック

| 区分 | 技術 | 選択理由 |
|------|------|---------|
| **言語** | Java 17 | 既存スキル活用, 安定性 |
| **フレームワーク** | Spring Boot 3.2 | 現行プロジェクト実績あり |
| **DB** | PostgreSQL 15 | 学習目的, 軽量 |
| **ORM** | Spring Data JPA | 開発速度向上 |
| **認証** | JWT (jjwt) | Stateless, 拡張性 |
| **API Docs** | SpringDoc OpenAPI | Swagger UI自動生成 |
| **ビルド** | Gradle (Kotlin DSL) | モダン, type-safe |

### 3.2 システム構成

```
[Client] 
   │
   ▼  HTTPS + JWT
[Spring Boot API] ─── [PostgreSQL]
   │
   ├── Swagger UI (開発時)
   └── Docker (運用時)
```

### 3.3 ERD (Entity-Relationship Diagram)

```
┌─────────┐       ┌──────────────┐       ┌─────────┐
│  users  │───1:N─│ crew_members │──N:1──│  crews  │
└─────────┘       └──────────────┘       └─────────┘
     │                                        │
     │ 1:N                                    │ 1:N
     ▼                                        ▼
┌────────────┐    ┌──────────┐    ┌────────────┐
│ schedules  │    │  tags    │    │ materials  │
└────────────┘    └──────────┘    └────────────┘
                                      │
                                      │ 1:N
                                      ▼
                                 ┌──────────┐
                                 │ comments │
                                 └──────────┘
```

---

## 4. 开发スケジュール

### 4.1 全体スケジュール (10週間)

```
Week  1-2  │████████████████████│ Phase 1: 認証システム
Week  3-4  │████████████████████│ Phase 1:  크루管理
Week  5-6  │████████████████████│ Phase 2:  日程管理
Week  7-8  │████████████████████│ Phase 2:  資料共有
Week  9-10 │████████████████████│ Phase 3:  拡張 + テスト
           └────────────────────┘
```

### 4.2 詳細タスク

#### **Week 1-2: 認証システム** 
- [ ] Spring Boot プロジェクト生成
- [ ] PostgreSQL 接続設定
- [ ] JPA Entity 設計 (User, RefreshToken)
- [ ]  회원가입 API + Validation
- [ ]  로그인 API + BCrypt
- [ ]  JWT Access/Refresh Token 実装
- [ ]  Security Config 設定
- [ ]  API 문서화 (Swagger) 設定

#### **Week 3-4:  크루管理**
- [ ] 크루 생성 API
- [ ]  초대코드 生成ロジック
- [ ] 크루 가입 API
- [ ] 크루 목록/詳細/会員一覧 API
- [ ] Exception Handling 体系化

#### **Week 5-6:  日程管理**
- [ ] 일정 CRUD API
- [ ]  チーム別 캘린더 조회
- [ ] 日付範囲 検索

#### **Week 7-8:  資料共有**
- [ ] 자료 등록/조회/삭제 API
- [ ] 태그 検索機能
- [ ]  コメント CRUD
- [ ] ダウンロード カウント

#### **Week 9-10:  拡張 + テスト**
- [ ] 알림 기능 (이메일)
- [ ]  Docker/Docker-Compose 設定
- [ ]  ユニットテスト
- [ ]  최종 정리 및 배포

---

## 5. チーム構成 (個人開発)

| 役割 | 担当 |
|------|------|
| PM / Developer | 신현욱 (시대생会员) |
| 技術 Mentor | - (自身) |

>  ※ 大学 프로젝트の場合、教授 및 先apelのフィードバックを接受的受ける

---

## 6. リスク管理

| リスク | 発生確率 | 影响度 | 対策 |
|--------|:-------:|:------:|------|
| PostgreSQL 学习曲线 | 中 | 低 | Oracle SQLとの类似性 활용 |
| JWT セキュリティ | 低 | 高 | バリデーション 강화, トークン期限設定 |
| 일정遅延 | 中 | 中 | 週次チェックポイント設定, 機能スコープ調整 |
| データ消失 | 低 | 高 | 정기 백업 (Docker volume) |

---

## 7. 成功指標 (KPI)

| 指標 | 目標値 |
|------|--------|
| 機能完成率 | Phase 1-2 100%, Phase 3 80% |
| API テスト カバレッジ | 70%以上 |
|  동아리 实际使用率 | 5명以上の定期 利用 |
| Swagger 문서 完成度 | 全API 文書化 |

---

## 8. Referencias

- [Spring Boot 3.2 Docs](https://docs.spring.io/spring-boot/docs/3.2.x/reference/html/)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [JWT Library (jjwt)](https://github.com/jwtk/jjwt)

---

## 9. 添付ファイル

- `SPEC.md` - 詳細仕様書
- `README.md` - 開発者 向指南書
- `docker-compose.yml` - PostgreSQL 実行環境
- `src/` - ソースコード一式

---

*作成日: 2026.06.20*  
*作成者: 신현욱*
