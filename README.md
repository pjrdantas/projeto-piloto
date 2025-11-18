Projeto Piloto - Hexagonal Auth Skeleton

- Java 17
- Spring Boot 3.x
- JWT stateless authentication
- Oracle (XEPDB1) datasource
- Clean Hexagonal structure (domain, application, infrastructure, adapters)

How to run:
1. Configure application.yml with your Oracle connection and a strong JWT secret
2. mvn spring-boot:run
3. Create initial data (roles, admin user) via SQL or a bootstrap component

👉 Swagger UI:
http://localhost:8080/swagger-ui/index.html

👉 OpenAPI JSON:
http://localhost:8080/v3/api-docs

👉 OpenAPI YAML:
http://localhost:8080/v3/api-docs.yaml