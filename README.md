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

ESTRUTURA DE PASTAS:

+---src
|   +---main
|   |   +---java
|   |   |   \---br
|   |   |       \---com
|   |   |           \---projeto
|   |   |               \---piloto
|   |   |                   |   ProjetoPilotoApplication.java
|   |   |                   |   
|   |   |                   +---adapter
|   |   |                   |   +---in
|   |   |                   |   |   \---web
|   |   |                   |   |       +---controller
|   |   |                   |   |       |       AuthController.java
|   |   |                   |   |       |       PermissionController.java
|   |   |                   |   |       |       RoleController.java
|   |   |                   |   |       |       UserController.java
|   |   |                   |   |       |       
|   |   |                   |   |       \---exception
|   |   |                   |   |               ErrorResponse.java
|   |   |                   |   |               GlobalExceptionHandler.java
|   |   |                   |   |               
|   |   |                   |   \---out
|   |   |                   |       \---jpa
|   |   |                   |           |   PermissionRepositoryAdapter.java
|   |   |                   |           |   RoleRepositoryAdapter.java
|   |   |                   |           |   UserRepositoryAdapter.java
|   |   |                   |           |   
|   |   |                   |           +---entity
|   |   |                   |           |       PermissionEntity.java
|   |   |                   |           |       RoleEntity.java
|   |   |                   |           |       
|   |   |                   |           +---mapper
|   |   |                   |           |       PermissionMapper.java
|   |   |                   |           |       RoleMapper.java
|   |   |                   |           |       UserDTOMapper.java
|   |   |                   |           |       
|   |   |                   |           \---repository
|   |   |                   |                   PermissionJpaRepository.java
|   |   |                   |                   RoleJpaRepository.java
|   |   |                   |                   SpringDataUserRepository.java
|   |   |                   |                   
|   |   |                   +---api
|   |   |                   |   \---dto
|   |   |                   |           AuthResponseDTO.java
|   |   |                   |           LoginRequestDTO.java
|   |   |                   |           PermissionDTO.java
|   |   |                   |           RefreshTokenRequestDTO.java
|   |   |                   |           RegisterRequestDTO.java
|   |   |                   |           RoleDTO.java
|   |   |                   |           RoleRequestDTO.java
|   |   |                   |           UserRequestDTO.java
|   |   |                   |           UsuarioDTO.java
|   |   |                   |           
|   |   |                   +---application
|   |   |                   |   \---usecase
|   |   |                   |           AuthService.java
|   |   |                   |           PermissionService.java
|   |   |                   |           RoleService.java
|   |   |                   |           UserDomainService.java
|   |   |                   |           UserService.java
|   |   |                   |           
|   |   |                   +---domain
|   |   |                   |   +---exception
|   |   |                   |   |       DomainException.java
|   |   |                   |   |       UserNotFoundException.java
|   |   |                   |   |       
|   |   |                   |   +---model
|   |   |                   |   |       Permission.java
|   |   |                   |   |       Role.java
|   |   |                   |   |       User.java
|   |   |                   |   |       
|   |   |                   |   \---port
|   |   |                   |       +---inbound
|   |   |                   |       |       AuthUseCasePort.java
|   |   |                   |       |       UserUseCasePort.java
|   |   |                   |       |       
|   |   |                   |       \---outbound
|   |   |                   |               PermissionRepositoryPort.java
|   |   |                   |               RoleRepositoryPort.java
|   |   |                   |               UserDomainRepositoryPort.java
|   |   |                   |               UserRepositoryPort.java
|   |   |                   |               
|   |   |                   \---infrastructure
|   |   |                       +---config
|   |   |                       |       SwaggerConfig.java
|   |   |                       |       
|   |   |                       \---security
|   |   |                               CustomUserDetailsService.java
|   |   |                               JwtAuthenticationFilter.java
|   |   |                               JwtUtil.java
|   |   |                               SecurityConfig.java
|   |   |                               
|   |   \---resources
|   |       |   application.yml
|   |       |   
|   |       \---META-INF
|   |               MANIFEST.MF
|   |               
|   \---test