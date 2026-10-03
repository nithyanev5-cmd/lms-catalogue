# AGENTS.md — Agent guidance for this repository

Purpose
- Short, focused instructions to help AI coding agents work productively in this workspace.

Quick commands
- **One-time DB setup** (run once before first backend start): `mysql -u root -p < db-setup/01-create-users.sql`
- Backend dev: `cd backend && mvn spring-boot:run`
- Backend tests: `cd backend && mvn test`
- Frontend dev: `cd frontend && npm install && npm run dev`
- Frontend build/preview: `cd frontend && npm run build` / `npm run preview`
- Frontend typecheck: `cd frontend && npm run typecheck` (Vite does not type-check; `npm run dev` succeeds with type errors)

Important files & links
- Overview/intent: [README.md](README.md)
- Backend project: [backend/pom.xml](backend/pom.xml)
- Frontend project: [frontend/package.json](frontend/package.json)
- Backend sources: [backend/src/main/java](backend/src/main/java)
- Backend tests: [backend/src/test/java](backend/src/test/java)
- Flyway migrations: [backend/src/main/resources/db/migration](backend/src/main/resources/db/migration)
- Demo dataset (Day 4): [backend/src/main/resources/db/demo/V90__demo_data.sql](backend/src/main/resources/db/demo/V90__demo_data.sql)
- DB user setup script: [db-setup/01-create-users.sql](db-setup/01-create-users.sql)

Frontend conventions
- Vite dev server: The frontend uses Vite. The dev server runs locally (default port 5173) and supports fast HMR. Start it with `cd frontend && npm install && npm run dev` (see Quick commands above).
- Proxying `/api`: During local development the Vite dev server proxies calls under `/api` to the backend (Spring Boot) to avoid CORS issues. The proxy is configured in [frontend/vite.config.ts](frontend/vite.config.ts). The backend runs via `cd backend && mvn spring-boot:run` (commonly on port 8080).
- Follow the project's React guidelines when changing frontend code: see [.github/instructions/react.instructions.md](.github/instructions/react.instructions.md) for component, TypeScript, and testing conventions.


Conventions & guardrails for agents
- Prefer updating `AGENTS.md` over creating a `.github/copilot-instructions.md` unless repository already uses that file.
- Always run `cd backend && mvn test` before making or proposing backend changes. Tests are the canonical safety net.
- **The backend uses MySQL 8.4** (`localhost:3306/lms_catalog`, user `lms_app`). It is NOT H2. The DB must exist before `mvn spring-boot:run` will succeed.
- `spring.jpa.hibernate.ddl-auto` is `validate`. Do NOT change entity mappings without adding a new Flyway migration; drift causes a boot-time failure.
- Flyway loads from both `classpath:db/migration` and `classpath:db/demo`. The demo profile (`V90__demo_data.sql`) adds messy production-like data (retired courses, over-capacity enrolments). It is always active unless you remove `classpath:db/demo` from `application.yml`.
- **Audit logging**: service classes must route all catalogue interactions through [`CatalogAuditLogger`](backend/src/main/java/com/globallearning/lms/catalog/audit/CatalogAuditLogger.java). Do NOT call SLF4J directly from service classes.
- **Mappers are hand-written** — the team deliberately avoided MapStruct so projections stay explicit. Add new fields to [`CourseMapper`](backend/src/main/java/com/globallearning/lms/catalog/web/mapper/CourseMapper.java) / [`EnrollmentMapper`](backend/src/main/java/com/globallearning/lms/catalog/web/mapper/EnrollmentMapper.java) manually.
- Spring Security 6 component style (no `WebSecurityConfigurerAdapter`). All `/api/**` endpoints are currently open; see [`SecurityConfig`](backend/src/main/java/com/globallearning/lms/catalog/config/SecurityConfig.java).
- Keep changes small and well-scoped. When touching public APIs (controllers, DTOs), update tests and mention migration/schema impact.
- Link to existing documentation rather than copy it: use README.md and migration files as the source-of-truth.

Architecture notes
- `catalog/service/` — business logic only. `CourseService`, `EnrollmentService`, `LearnerService`, `LegacyCourseCodeParser`.
- `catalog/web/` — controllers, DTOs, hand-written mappers, two exception handlers (`CatalogExceptionHandler`, `EnrollmentExceptionHandler`).
- `LegacyCourseCodeParser` — parses codes like `ENGG2104B`, `CS1007`, `MATH3220A` (format: `[FACULTY 2-4 alpha][LEVEL digit][SEQ 3 digit][optional revision letter]`). It derives `faculty` and `level` from the code; this feeds the `faculty` field on `CourseResponse`.
- Frontend `src/constants.ts` owns `LEVEL_LABELS`, `LEVEL_FILTERS`, `formatLevel()`, `formatCredits()`. Use these helpers; do not format levels or credits inline in components.
- Frontend `src/api/catalog.ts` exports `fetchCourses({ level, keyword })` and `fetchCourseByCode(code)`. All backend calls go through here.

Typical agent tasks and where to start
- Small bugfix in API: start at [backend/src/main/java](backend/src/main/java) and run `mvn test`.
- Add a UI tweak: modify [frontend/src](frontend/src) and run `npm run dev` to verify in the browser.
- DB/schema work: inspect [backend/src/main/resources/db/migration](backend/src/main/resources/db/migration) first.

If you add or update this file
- Keep it minimal. Add links to new top-level docs (CONTRIBUTING.md, ARCHITECTURE.md) rather than duplicating them.

Contact / follow-up
- If you want more granular agent instructions (separate frontend/backend instructions, or automated hooks), ask to `/create-agent` with the desired scope.
