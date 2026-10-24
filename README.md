# Inkloom AI

Brand-governance product prototype for reviewing content against approved product facts and guidelines.

## Repository layout

    InkloomAI/
    ├── frontend/          React + Vite application
    │   └── src/           App.jsx, main.jsx and styles.css
    ├── backend/           Spring Boot REST API and MongoDB persistence
    │   ├── pom.xml
    │   └── src/main/java/
    ├── infra/              Local Docker Compose configuration
    ├── .github/workflows/  Backend test workflow
    └── vercel.json         Vercel builds frontend/ only

## Run the frontend

    cd frontend
    npm install
    npm run dev

Vite prints the local development URL, usually http://localhost:5173.

## Run the backend and MongoDB

From the repository root, with Docker Desktop and Docker Compose installed:

    docker compose -f infra/docker-compose.yml up --build

The API is bound to localhost:8080 and MongoDB to localhost:27017. Check the API at http://localhost:8080/api/health.

To run the backend without Docker, start MongoDB locally, then:

    cd backend
    mvn spring-boot:run

The backend needs JDK 17 and Maven 3.9+. See backend/README.md for routes, request examples and limitations.

## Backend API currently available

- GET /api/health
- GET /api/submissions (optional status and submittedBy filters)
- GET /api/submissions/{id}
- POST /api/submissions
- POST /api/submissions/{id}/decisions
- POST /api/submissions/{id}/resubmit

## Deployment

Vercel builds the frontend from frontend/ and publishes frontend/dist. The Spring Boot API is separate and is not deployed by Vercel; it currently has local Docker Compose support.

## Current status and security

- The frontend has role-selection demo sign-in, company/product catalogue, local product-specific phrase checks, creator submissions and a company review panel.
- The backend now persists submission drafts, findings, decisions, comments, events and revision history in MongoDB.
- The frontend is **not connected to the backend yet**. The current website still uses browser memory for its workflow.
- The demo role selector is not real authentication. The backend does not yet enforce identity, role authorization or company-level data isolation. Do not expose the API publicly or use it with confidential content before those protections are implemented.
- Product records and claim facts are illustrative data. Local phrase checks are not AI verification or a compliance determination.
