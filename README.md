# LMS Course Catalogue

A working slice of the Global Learning LMS platform: the public course catalogue,
with learner management and enrolment support. Spring Boot 3.5 + MySQL 8.4 on the back, React 19 + TypeScript on the front.

> [!NOTE]
> This is a training codebase. Create the MySQL database and user before starting the backend
> (see [db-setup/01-create-users.sql](db-setup/01-create-users.sql)).

## What is in here

```
backend/                     Spring Boot 3.5, Java 21
  CatalogApplication         Entry point; component scan starts at com.globallearning.lms
  catalog/domain/            Course, CourseLevel, Learner, Enrollment, EnrollmentStatus
  catalog/repository/        Derived queries plus one native keyword search
  catalog/service/CourseService          Listing, lookup, update, deactivation, archive, reactivation
  catalog/service/EnrollmentService      Enrol a learner; enforces capacity and duplicate checks
  catalog/service/LearnerService         Learner lookup
  catalog/service/LegacyCourseCodeParser Dense logic carried over from the 2014 catalogue
  catalog/audit/CatalogAuditLogger       House component all catalogue reads route through
  catalog/web/               CourseController, EnrollmentController, LearnerController,
                             DTOs, hand-written mappers, error handlers
  catalog/config/SecurityConfig          Spring Security 6, component style
  resources/db/migration/    V1 schema · V2 seed courses · V3 capacity · V4 instructor email
                             V5 learners + enrolments · V6 course archive columns
  resources/db/demo/         V90 demo dataset (messy production-like data, used in Day 4)
frontend/                    React 19 + TypeScript + Vite 8, catalogue listing screen
  src/components/            CourseCard, CourseList, LevelFilter
```

## Running it

The backend connects to a MySQL server on `localhost:3306`. Create the database and
user once with the setup script before starting the app for the first time:

```bash
mysql -u root -p < db-setup/01-create-users.sql
```

`spring.jpa.hibernate.ddl-auto` is set to `validate`, which means the entities and the
migrations must agree. If they drift, the application does not boot. That is deliberate:
a schema mistake fails loudly at startup rather than quietly at runtime.

**1. Start the API**

```bash
cd backend
mvn spring-boot:run
```

**2. Start the UI**

```bash
cd frontend
npm install
npm run dev
```

Open http://localhost:5173. The Vite dev server proxies `/api` to port 8080.

## The API

### Courses

| Endpoint | What it does |
|---|---|
| `GET /api/courses` | Active, non-archived courses ordered by code. Optional `level` and `q` filters |
| `GET /api/courses/{code}` | One course in detail, including `capacity`, `instructorEmail`, and a derived `faculty` |
| `PUT /api/courses/{code}` | Full replace. `title`, `credits`, and `capacity` are required; omitting a field clears it |
| `POST /api/courses/{code}/deactivate` | Sets `active` to false |
| `POST /api/courses/{code}/reactivate` | Sets `active` back to true (errors if already active or archived) |
| `POST /api/courses/{code}/archive` | Archives the course. Requires `{ "reason": "..." }` body |

The listing response is narrower than the detail response: `capacity` and `instructorEmail` appear only in the detail.

### Learners and Enrolments

| Endpoint | What it does |
|---|---|
| `POST /api/enrollments` | Enrol a learner. Body: `{ "courseCode": "CS2110", "learnerRef": "L-00001" }` |
| `GET /api/learners/{learnerRef}/enrollments` | All enrolments for a learner, newest first |

## Seed data

**Courses** — eight rows from `V2__seed_courses.sql`, every one with a `capacity` of 30:

| Code | Level | Credits | Active |
|---|---|---|---|
| `CS1007` | FOUNDATION | 10 | yes |
| `CS2110` | INTERMEDIATE | 15 | yes |
| `CS3401` | ADVANCED | 20 | yes |
| `ENGG2104B` | INTERMEDIATE | 15 | yes |
| `MATH3220A` | ADVANCED | 20 | yes |
| `DATA1105` | FOUNDATION | 10 | yes |
| `WEB2208` | INTERMEDIATE | 15 | yes |
| `CS4999` | ADVANCED | 20 | **no** — withdrawn from the 2019 catalogue |

`CS4999` is inactive, so it is absent from the listing but still reachable by code.

**Learners** — ten rows from `V5__create_learners_and_enrollments.sql`:
`L-00001` through `L-00010` (first/last name and email set; no enrolments seeded by default).

The Day 4 demo dataset (`V90__demo_data.sql`, loaded from `db/demo/`) adds messy
production-like data on top: retired courses, over-capacity enrolments, and a column added
out-of-band. It is only active when the demo Spring profile is enabled.

## Checking it works

```bash
# Course listing and filtering
curl http://localhost:8080/api/courses
curl "http://localhost:8080/api/courses?level=ADVANCED"
curl "http://localhost:8080/api/courses?q=design"
curl http://localhost:8080/api/courses/CS2110

# Enrol a learner
curl -X POST http://localhost:8080/api/enrollments \
     -H "Content-Type: application/json" \
     -d '{"courseCode":"CS2110","learnerRef":"L-00001"}'

# List a learner's enrolments
curl http://localhost:8080/api/learners/L-00001/enrollments

# Archive a course
curl -X POST http://localhost:8080/api/courses/CS4999/archive \
     -H "Content-Type: application/json" \
     -d '{"reason":"Withdrawn permanently"}'
```

Run the tests:

```bash
cd backend && mvn test
cd frontend && npm run typecheck
```

All tests should pass before you start any exercise. If they don't, fix that first — you
cannot tell what an agent broke if the baseline was already broken.

## Prerequisites

- Java 21+ and Maven 3.6+ (for the backend)
- Node 20.19+ or 22.12+ and `npm` (for the frontend) — required by Vite 8

## Where to look first

- Backend entry: [backend/src/main/java/com/globallearning/lms/CatalogApplication.java](backend/src/main/java/com/globallearning/lms/CatalogApplication.java)
- Web controllers: [backend/src/main/java/com/globallearning/lms/catalog/web](backend/src/main/java/com/globallearning/lms/catalog/web)
- Flyway migrations: [backend/src/main/resources/db/migration](backend/src/main/resources/db/migration)
- Frontend entry: [frontend/src/main.tsx](frontend/src/main.tsx)
- Frontend components: [frontend/src/components](frontend/src/components)

## Troubleshooting

> [!WARNING]
> If the backend fails to start with JPA validation errors, the entities and migrations have
> drifted. Inspect the migrations under `db/migration/` and ensure schema changes are
> reflected there before re-running.
