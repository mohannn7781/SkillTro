# SkillTro

SkillTro is a React + Vite application backed by Java 17, Spring Boot, Spring Data JPA/Hibernate, and PostgreSQL. It connects workers and employers using worker-confirmed skills, transparent required-skill overlap, assessment knowledge checks, and job applications.

## Requirements

- Java 17+
- Java 17+ (the Maven Wrapper downloads the project-pinned Maven 3.9.9 distribution on first use; no global Maven installation is needed)
- Node.js 20+ and npm
- PostgreSQL 14+

## PostgreSQL setup

Create a local PostgreSQL database and role (run these statements as a PostgreSQL administrator):

```sql
CREATE USER skilltro WITH PASSWORD 'choose-a-local-password';
CREATE DATABASE skilltro OWNER skilltro;
```

If Docker Compose is installed, the included service can start PostgreSQL instead:

```powershell
$env:DATABASE_PASSWORD = 'choose-a-local-password'
docker compose up -d postgres
```

The application reads `DATABASE_URL`, `DATABASE_USERNAME`, and `DATABASE_PASSWORD` from its environment. The safe local defaults point to `localhost:5432/skilltro`; set a password before starting the backend. Hibernate creates/updates the schema on startup (`ddl-auto: update`). For example, in PowerShell:

```powershell
$env:DATABASE_URL = 'jdbc:postgresql://localhost:5432/skilltro'
$env:DATABASE_USERNAME = 'skilltro'
$env:DATABASE_PASSWORD = 'choose-a-local-password'
```

Do not commit `.env` or real credentials. `.env.example` is a template; Spring does not automatically load it.

## Start without PostgreSQL (development only)

Select the temporary in-memory profile explicitly:

```powershell
.\mvnw.cmd "-Dspring-boot.run.profiles=dev-memory" spring-boot:run
```

This profile does not require PostgreSQL or database credentials. It keeps records only in process memory; **all user-created data is lost when the backend stops or restarts**, and demo seed data is recreated on startup. Do not use this profile for production or persistence checks. The default profile remains `postgres`.

Start the frontend in a second terminal:

```powershell
npm install
npm run dev
```

## Start with PostgreSQL

After PostgreSQL is initialized and running, start the backend with its credentials in the environment:

Backend:

```powershell
$env:DATABASE_URL = 'jdbc:postgresql://localhost:5432/skilltro'
$env:DATABASE_USERNAME = 'skilltro'
$env:DATABASE_PASSWORD = 'choose-a-local-password'
.\mvnw.cmd spring-boot:run
```

Frontend (second terminal):

```powershell
npm install
npm run dev
```

Open the Vite URL printed in the terminal. Vite proxies `/api` requests to `http://localhost:8080`.

## Tests

Run the backend suite with `.\mvnw.cmd test`. The in-memory API tests exercise registration validation, authentication and role failures, profile updates, skill suggestions and confirmation, assessment submission, job matching and application submission, employer status updates, and worker status retrieval. Repository tests cover IDs, timestamps, lookup, uniqueness, relations, and deletion. These tests do not verify PostgreSQL persistence; that requires a running PostgreSQL server. The wrapper downloads Maven 3.9.9 from Maven Central on first use and does not require a global Maven installation.

## Demo accounts

The backend seeds these accounts and three clearly labeled demo opportunities on an empty database:

- Worker: `worker.demo@skilltro.local` / `worker-demo-123`
- Employer: `employer.demo@skilltro.local` / `employer-demo-123`

The seed data includes skill records and assessment questions. Create another account through the UI to exercise registration and profile setup.

## Product behavior

- Passwords are hashed with BCrypt. Authentication uses an HTTP session cookie, and worker/employer endpoints enforce role and job ownership.
- Worker skill suggestions use simple phrase matching against occupation and work description. A worker must confirm a skill before it contributes to matching; rejected suggestions are stored and excluded.
- Match percentage is `matched required skills / total required skills × 100`. Preferred skills are displayed separately. Missing required skills are called out explicitly; match percentage is not a hiring probability and cannot supersede safety, legal, or licensing requirements.
- Multiple-choice assessment attempts measure quiz answers only. They are not proof of practical competence.
- All created job listings are marked as demo opportunities. Demo data is only seeded for a fresh database.

## API overview

REST JSON routes live under `/api`: `/auth/*`, `/worker/*`, `/employer/*`, and `/skills`. The schema is managed by JPA entities in `src/main/java/org/skilltro/Domain.java` and initialized with demo data in `Api.java`.

## Verification status

Build and PostgreSQL persistence must be verified in an environment with Java 17 and a reachable PostgreSQL service. The checked-in Maven Wrapper supplies Maven 3.9.9. Do not treat the presence of source files or the included seed data as evidence that PostgreSQL is running.
