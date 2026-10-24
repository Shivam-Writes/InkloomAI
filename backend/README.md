# Backend

The current `main` branch does not yet contain the Spring Boot backend source. Add backend implementation here rather than placing Java sources at the repository root.

Planned structure:

```text
backend/
  pom.xml
  src/main/java/...
  src/main/resources/application.yml
  src/test/java/...
```

The planned service uses Spring Boot REST APIs, Spring Security for server-side authentication/roles, and MongoDB persistence. This folder is a structure placeholder only; no API or authentication is implemented here yet. Do not commit credentials or production secrets.
