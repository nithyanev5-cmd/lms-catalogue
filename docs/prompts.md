# Day 7 — Testing, Debugging and Documentation with AI Agents

### Exercise 1: Debug a defect to a confirmed root cause



#### 3. Hand the trace to the agent — the trace, not your description of it
```
Filtering the course catalogue by level returns 500. The unfiltered catalogue works. Here is the full stack trace from the backend:

#terminalSelection

Explain what is actually null and why it is null for this data. Do not change any code yet.
```

#### 4. Ask for possible fixes and then fix it

```
What are some possible ways to fix this bug? Suggest two or three possible solutions and give your recommendation for the best approach
```

```
Implement your recommended solution
```

---

### Exercise 2: A test-writer agent, and the guardrails that make it reviewable


#### 2. Write unit tests that would have caught the bug in the previous exercise

```
Write unit tests for CourseService.listActive. Cover all four dispatch branches:
- no level and no keyword
- a level only
- a keyword only
- both together

Also cover:
- a level whose courses have a null averageRating, which must not throw
- the ordering the method promises
- verify that CatalogAuditLogger is called once with the result count

Think of any other edge cases and corner cases that might break the method and write tests for them.

Mock the repositories and the audit logger. Do not start a Spring context. Do not modify anything under src/main.
```

---

### Exercise 3: Audit the tests, measure the coverage


#### 1. Audit Existing Tests

```
Review every test under backend/src/test. For each one, answer three questions:
1. Which of its assertions would still pass if the production code it covers were wrong?
2. Does it verify any interaction that matters, or only that nothing threw?
3. Does it assert a behaviour that is itself a bug?

List the tests you would delete or rewrite, worst first. Do not change any code.
```

#### 2. Measure Coverage of the Tests

```
Add the jacoco-maven-plugin to backend/pom.xml (version 0.8.14) with the prepare-agent and report goals bound to the default phases. Then run cd backend && mvn test jacoco:report.
```
---

### Exercise 4: Document the API, then drive it from Postman


#### 1. Generate the specification

```
Add springdoc-openapi to the backend so this API publishes an OpenAPI 3 document and a Swagger UI. Use a 2.9.0 version compatible with Spring Boot 3.

Annotate the controllers with @Tag, @Operation and @ApiResponse, including the error status codes each endpoint actually returns. Read CatalogExceptionHandler and EnrollmentExceptionHandler for those.
```
---

### Appendix

### `.github/agents/test-writer.agent.md`

````markdown
---
name: Test Writer
description: Writes and runs tests for this Spring Boot codebase
tools: [execute, read, agent, edit, search, todo]
---

You are an expert in writing tests for the LMS catalogue backend. You do not write features.

Before writing anything, read the code under test and the tests that already exist for
neighbouring classes, and match their style.

You may create and edit files under `backend/src/test`. You may run `cd backend && mvn test`.
You may not edit anything under `backend/src/main` — if a test cannot be written without a
production change, stop and explain why.

When you hand back, report three things: what you covered, what you could not cover and why,
and any behaviour you found that looks wrong but that you have left alone.

# Test conventions

## Frameworks

- JUnit 5, Mockito and AssertJ only. They arrive through `spring-boot-starter-test`.
- Do not add a test dependency without being asked. There is no H2 and no Testcontainers in
  this project, and adding one silently changes what the tests prove.

## Shape

- Arrange, Act, Assert, with a blank line between the three.
- One behaviour per test. Name the method for the behaviour, not for the method under test.
- Unit tests mock repositories and never start a Spring context.

## Assertions

- Never assert on a value the test itself computed. Write the expected value as a literal.
- Every mocked interaction that matters must be verified. A test that only proves nothing
  threw is not a test.
- Assert the response body, not only the status code.

## Integration tests

- `@SpringBootTest` in this project talks to the real MySQL on `localhost:3306`. There is no
  test datasource and no `src/test/resources`.
- Every `@SpringBootTest` class that writes must be `@Transactional`, so that the transaction
  rolls back. A test that leaves rows behind fails on its second run.
- Do not use these pairs as fixtures. They carry duplicate ACTIVE enrolment rows and throw
  before reaching any application logic: CS1007/L-00001, CS1007/L-00002, CS3401/L-00005.
- `L-00042` to `L-00048` are enrolled on WEB2208 and unreviewed. Use them to write.

## Rules

- Never change anything under `src/main` to make a test pass. If a test cannot pass without a
  production change, stop and say so.
- Report what you could not cover. Do not invent an assertion to fill a gap.
- `cd backend && mvn test` must be green before you hand back.

````