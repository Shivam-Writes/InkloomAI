# Inkloom AI Backend

Spring Boot REST API for the content submission and company review lifecycle. It stores submissions, product context, findings, reviewer feedback, decisions, events and prior versions in MongoDB.

## Requirements

- JDK 17 or later
- Maven 3.9+
- MongoDB 8.x, or Docker with Docker Compose

## Run with Docker Compose

From the repository root:

    docker compose -f infra/docker-compose.yml up --build

The API is available at http://localhost:8080. MongoDB is bound to localhost:27017. Check the API at http://localhost:8080/api/health.

Stop services with Ctrl+C; to stop containers started in the background, use:

    docker compose -f infra/docker-compose.yml down

MongoDB data stays in a named Docker volume. To remove the saved local database as well, use Docker Compose's \`down -v\` command.

## Run the API directly

Start MongoDB locally, then from the repository root:

    cd backend
    mvn spring-boot:run

Default MongoDB URI: mongodb://localhost:27017/inkloomai. Set MONGODB_URI to override it.

Run tests:

    mvn test

## API routes

- GET /api/health — service status
- GET /api/submissions — list submissions; optional query parameters \`status\` and \`submittedBy\`
- GET /api/submissions/{id} — fetch one submission
- POST /api/submissions — create a submission and put it in the company review queue
- POST /api/submissions/{id}/decisions — approve, reject, or request changes
- POST /api/submissions/{id}/resubmit — archive the previous version and resubmit the revised draft

Example create request:

    {
      "name": "NovaBook launch script",
      "companyId": "northstar",
      "companyName": "Northstar Devices",
      "productId": "nova-book-pro",
      "productName": "NovaBook Pro 14",
      "model": "NBP-14",
      "contentType": "Script / ad copy",
      "content": "Example draft text",
      "submittedBy": "Creator Name",
      "submittedByEmail": "creator@example.test",
      "findings": []
    }

Example decision:

    {
      "decision": "Request changes",
      "comment": "Please support this performance claim with an approved source.",
      "reviewerName": "Company reviewer"
    }

A comment is required for a rejection or a request for changes. Only submissions awaiting company review can be decided; resubmission is allowed only from the Changes requested state. A resubmission archives the earlier version and increments the version number.

## Current limitations

This is a backend foundation for local development. It does not yet implement authentication, server-enforced roles, organization-level data isolation, rate limiting, AI verification or product-source management. Reviewer/creator names in request bodies are not authenticated identities. CORS is not authentication. The frontend does not yet call this API. Do not expose this service publicly or use real/confidential content until authentication and authorization are added.
