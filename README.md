# Inkloom AI

Brand-governance prototype for reviewing content against approved product facts and guidelines.

## Repository layout

```text
InkloomAI/
├── frontend/          # React + Vite application
│   ├── src/
│   │   ├── App.jsx
│   │   ├── main.jsx
│   │   └── styles.css
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
├── backend/            # Reserved for the Spring Boot API
├── infra/              # Deployment and infrastructure resources
├── .gitignore
├── README.md
└── vercel.json
```

## Run the frontend locally

Open a terminal at the repository root:

```bash
cd frontend
npm install
npm run dev
```

Vite prints the local development URL (usually `http://localhost:5173`).

To create a production build locally:

```bash
npm run build
npm run preview
```

## Deployment

Vercel is configured at the repository root to install dependencies from `frontend/`, run the frontend production build, and publish `frontend/dist`.

## Current implementation status

- The frontend includes a role-selection demo sign-in, company and product catalogue screens, content-review UI and approval UI.
- Company names, products, product facts, submissions and review results are illustrative sample data.
- The role selector is not authentication. Server-side authorization, real company-owned sources, AI verification and persistent database records are not connected yet.
- `backend/` is currently a placeholder for the planned Spring Boot REST API. Java backend source and a backend `pom.xml` have not been added to this repository yet.
- `infra/` is reserved for deployment and infrastructure configuration.
