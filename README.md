# Inkloom AI

Brand-governance product prototype for reviewing content against approved product facts and guidelines.

## Repository layout

    InkloomAI/
    ├── frontend/          React + Vite app and role-aware UI
    ├── backend/           Spring Boot REST API with server-side role checks
    ├── infra/             Local Docker Compose configuration
    ├── .github/workflows/ Frontend build and backend test checks
    └── vercel.json        Vercel builds frontend/ only

## Run the frontend

    cd frontend
    npm install
    npm run dev

The frontend uses demo login shortcuts. UI role rules control what the demo session can see, but they are not identity authentication.

## Run the backend and MongoDB

1. Copy .env.example to .env and replace every sample password.
2. From the repository root, run:

       docker compose -f infra/docker-compose.yml up --build

3. Check the public health route at http://localhost:8080/api/health.

The backend requires JDK 17 and Maven 3.9+. See backend/README.md for direct-run instructions, local role accounts and endpoint permissions.

## Role access control

- Admin: manage users/roles in the demo UI, workspaces, guidelines/sources, submit/fix content, and make final decisions.
- Brand Manager: manage demo workspaces, guidelines/sources, submit/fix content, and request changes; cannot approve or reject.
- Content Creator: submit content, apply fixes and revisions, view findings and use Clarity Heatmap signals as view-only.
- Reviewer: view findings, request changes, approve/reject; Clarity Heatmap is view-only.

The permission matrix is in Team Management (Admin demo role). The same policy map drives frontend navigation and action visibility. Demo member/workspace/policy edits remain browser state and are not persisted.

## Backend authorization

The Spring Boot API now requires HTTP Basic authentication for submission endpoints and applies method-level role checks. Role passwords come from environment variables and must not be committed. Content Creators can list or retrieve only submissions created by their authenticated username.

Important: this is an initial RBAC foundation, not a finished multi-tenant identity system. Admin, Brand Manager and Reviewer records are not isolated by organization yet; persistent account management and frontend-to-backend authentication are still pending. Do not expose the API publicly or use confidential company content until organization-level authorization, TLS, password rotation and production identity management are implemented.

## Deployment

Vercel deploys the frontend only. The Spring Boot API and MongoDB must be run/deployed separately.
