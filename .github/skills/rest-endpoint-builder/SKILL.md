---
name: rest-endpoint-builder
description: Add a REST endpoint to this repository following its house shape - controller, service, domain exception, @RestControllerAdvice entry, request and response DTOs, the hand-written mapper, and the audit line. Use when a task adds or changes an endpoint, a controller, a DTO, an exception handler or an HTTP status code anywhere under catalog/web.
---

## Purpose

Provide a concise, opinionated recipe for adding a new REST endpoint to this repository following existing conventions (Spring Boot backend, Flyway migrations, layered service/repository/controller design, and repository-wide test rules).

## Scope

- Workspace-scoped skill for contributors to the `backend` module.
- Best for adding CRUD or query endpoints that interact with the database or service layer.

## Quick summary

- Steps: design API → add DTOs/entities → repository → service → controller → tests → migrations (if DB change) → run `mvn test`.
- Decision points: schema change? auth required? sync vs async? pagination/sizing?
- Quality checks: unit + integration tests, Flyway migration present for schema changes, consistent logging/validation, API contract documented.

## Repository-specific assumptions

- Backend is a Maven Spring Boot app at `backend/`.
- Flyway migrations live in `backend/src/main/resources/db/migration` and must be updated for any schema change.
- Run tests with `cd backend && mvn test` (tests are the canonical safety net — see AGENTS.md).
- Domain models live under `backend/src/main/java/com/globallearning/lms/catalog/domain`.
- Repositories live under `.../repository`, services under `.../service`, controllers under `.../web`.

## Step-by-step recipe

1. Design the API contract
   - Decide URL, HTTP method(s), path params, query params, request and response shapes.
   - Record the contract in the issue/PR description and in any API docs used by your team.

2. DTOs and validation
   - Add request/response DTOs in `.../web/dto` (create the package if absent).
   - Use javax/Jakarta validation annotations (`@NotNull`, `@Size`, `@Email`, etc.) on request DTOs.

3. Domain/entity changes (only if needed)
   - If you must change DB schema, add a Flyway migration script in `backend/src/main/resources/db/migration` named with the next `V{N}__description.sql`.
   - Add or update entity classes in `.../domain` with JPA annotations and a matching migration.
   - Keep `spring.jpa.hibernate.ddl-auto` as `validate` — do not rely on Hibernate to change schema at runtime.

4. Repository
   - Add a Spring Data JPA `interface` in `.../repository` following existing repository patterns.
   - Use method names or `@Query` only when necessary; prefer repository methods returning `Optional<T>` for single-entity fetches.

5. Service layer
   - Introduce a service class in `.../service` that contains business logic and transaction boundaries (`@Service`, `@Transactional` where appropriate).
   - Keep controllers thin; services should orchestrate repositories, mappers, and domain rules.

6. Controller (web)
   - Add a controller in `.../web` annotated with `@RestController` and request mapping (e.g., `@RequestMapping("/api/courses")`).
   - Accept DTOs, validate with `@Valid`, and return appropriate HTTP status codes (`200`, `201`, `204`, `400`, `404`, `500`).
   - Handle exceptions via existing global exception handlers, or add a specific `@ControllerAdvice` if needed.

7. Mapping
   - Reuse existing mapper patterns (MapStruct or manual) used elsewhere in the repo. Add a mapper in `.../service/mapper` or `.../web/mapper` per project conventions.

8. Tests
   - Unit tests: add JUnit tests for the service and repository layers under `backend/src/test/java/...` following existing test patterns (mock dependencies and assert behavior).
   - Controller tests / integration tests: use the existing controller test patterns (see `CourseControllerTest.java`) to assert request/response behavior.
   - If you added a migration, add integration tests that exercise the DB migration (the test setup uses an H2 in-memory DB seeded by Flyway).
   - Run `cd backend && mvn test` and fix failing tests before opening a PR.

9. Documentation & PR
   - Update README/API docs if applicable.
   - In the PR description, list the files added/changed, the migration script (if any), and how to test locally.

## Decision points & branching logic

- Schema change? → Yes: create Flyway migration + entity changes + integration tests. No: add DTOs/service/controller only.
- Auth required? → Integrate with security filter or annotate controller methods to require roles (follow existing security conventions).
- Return type large list? → Implement pagination parameters (`page`, `size`) and return a paged response.

## Common pitfalls & guidance

- Do not rely on Hibernate to alter schema at runtime — always add a migration for schema changes.
- Keep controller methods small and delegate to services.
- Add `@Transactional` at the service-level only when the operation needs it.
- Avoid leaking entities to the web layer; map to DTOs.
