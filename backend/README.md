# Inkloom AI Backend

Spring Boot REST API for content submissions, company review and MongoDB persistence, protected with server-side role-based access checks.

## Requirements

- JDK 17 or later
- Maven 3.9+
- MongoDB 8.x, or Docker with Docker Compose

## Configure local credentials

From the repository root, copy .env.example to .env and replace every sample password before starting the API. Do not commit .env. For direct Maven runs, export the four RBAC_*_PASSWORD environment variables in your shell.

Default local usernames:
- alexander.cross@demo.inkloom.test — Admin
- elena.rostova@demo.inkloom.test — Brand Manager
- marcus.vance@demo.inkloom.test — Content Creator
- sarah.jenkins@demo.inkloom.test — Reviewer

Passwords are not stored in source code. The API refuses to start if a required role password is missing.

## Run with Docker Compose

    docker compose -f infra/docker-compose.yml up --build

The API is bound to localhost:8080 and MongoDB to localhost:27017. Health is public at http://localhost:8080/api/health. Submission routes require HTTP Basic authentication.

## Run directly

Start MongoDB, set the four RBAC_*_PASSWORD environment variables, then run from the repository root:

    cd backend
    mvn spring-boot:run

Default MongoDB URI: mongodb://localhost:27017/inkloomai. Set MONGODB_URI to override it.

Run tests:

    mvn test

## Role-protected routes

- GET /api/health — public health status
- GET /api/submissions and GET /api/submissions/{id} — authenticated roles; Content Creators only see submissions created under their authenticated username
- POST /api/submissions — Admin, Brand Manager, Content Creator
- POST /api/submissions/{id}/decisions — Admin and Reviewer can approve/reject/request changes; Brand Manager can only request changes
- POST /api/submissions/{id}/resubmit — Admin, Brand Manager, Content Creator

The server uses the authenticated principal as the actor instead of trusting the actor's name in the request body. It rejects a second decision after a final decision has already been recorded.

## Important limits

This is an initial server-side RBAC foundation using HTTP Basic and environment-configured in-memory accounts. It is not a production identity system. The team-management screen and workspace/guideline editor still update browser state only; they do not create persistent accounts, separate company data stores, or database-backed policies. Organization-level data isolation for Admins, Brand Managers and Reviewers is not yet implemented, and the React frontend is not connected to the protected API. Do not expose this API publicly until TLS, persistent identity management, organization-scoped authorization, rate limiting and credential rotation are in place. CORS is not an authorization boundary.
