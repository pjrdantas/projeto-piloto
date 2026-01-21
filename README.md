# 🚀 Projeto Piloto - Hexagonal Auth Skeleton

Este projeto é um esqueleto de autenticação e gestão de aplicativos desenvolvido com **Spring Boot 3**, utilizando os princípios da **Arquitetura Hexagonal (Ports & Adapters)** para garantir um sistema desacoplado, testável e de fácil manutenção.

---

** 🛠️ Tecnologias e Especificações **
- **Java:** 17
- **Spring Boot:** 3.x
- **Autenticação:** JWT Stateless (JSON Web Token)
- **Banco de Dados Local:** Oracle (Service Name: XEPDB1)
- **Banco de Dados Nuvem:** PostgreSQL (AWS RDS)
- **Migrações:** Flyway com suporte a baseline.
- **Segurança:** Spring Security com configuração de filtros customizados.

---

** 📂 Estrutura de Diretórios (Hierarquia Completa) **

A organização segue o padrão da arquitetura hexagonal, separando o domínio da infraestrutura e dos adaptadores de entrada e saída.

```text
C:.
├── 📄 deploy.bat                # Automação de build e deploy Docker local
├── 📄 Dockerfile                # Configuração da imagem do container
├── 📄 pom.xml                   # Gerenciador de dependências Maven
├── 📄 README.md                 # Documentação do projeto
├── 📁 .github/workflows/        # Pipeline de CI/CD (GitHub Actions)
│   └── ci-cd.yml
├── 📁 k8s/                      # Manifestos de Orquestração Kubernetes
│   ├── deployment.yaml
│   └── nginx-config.yaml
├── 📁 src/main/java/br/com/projeto/piloto/
│   ├── 🚀 ProjetoPilotoApplication.java
│   │
│   ├── 🔌 adapter/              # Camada de Adaptadores
│   │   ├── 📥 in/web/           # Entrada via REST API
│   │   │   ├── 🎮 controller/   # Aplicativos, Auth, Perfil, Permissao, Usuario
│   │   │   ├── 📦 dto/          # Requests, Responses e Resumos (Data Transfer Objects)
│   │   │   └── ⚠️ exception/    # ErrorResponse e GlobalExceptionHandler
│   │   └── 📤 out/jpa/          # Saída via Persistência (JPA)
│   │       ├── 💾 entity/       # Entidades mapeadas (Aplicativo, Perfil, etc.)
│   │       ├── 🗺️ mapper/       # Mapeadores MapStruct/ModelMapper
│   │       ├── 🏛️ repository/   # Interfaces Spring Data JPA
│   │       └── 🔌 AdapterImpl/   # Implementações dos Ports de saída
│   │
│   ├── 🏛️ application/         # Camada de Aplicação
│   │   └── ⚙️ usecase/          # Interactors (Implementação da lógica de negócio)
│   │
│   ├── 🏗️ domain/              # Camada de Domínio (O Coração do Sistema)
│   │   ├── ⚠️ exception/        # Exceções de Domínio (DomainException)
│   │   ├── 💎 model/           # Modelos de Domínio Puros
│   │   └── 🏁 port/             # Interfaces de Entrada (Inbound) e Saída (Outbound)
│   │
│   └── ⚙️ infrastructure/      # Configurações Técnicas
│       ├── 📝 config/           # AuthProperties, JwtProperties, SwaggerConfig
│       └── 🔒 security/         # JwtUtil, SecurityConfig, CustomUserDetailsService
│
├── 📁 src/main/resources/
│   ├── 📄 application.yml       # Configurações de Profiles (dev, docker, prod)
│   └── 📁 db/migration/         # Scripts Flyway (V1__create_tables.sql)
│
└── 📁 src/test/java/            # Testes Unitários e de Integração
    └── controller/              # Testes específicos para os endpoints Web

```

---

** 🚀 Como Executar a Aplicação **

** 1. Preparação do Ambiente **

Certifique-se de configurar o arquivo src/main/resources/application.yml com as credenciais corretas:

Oracle Connection: Ajuste a URL, username e password.

JWT Secret: Defina uma chave forte no campo auth.jwt.secret.



** 2. Execução via Maven **

Para rodar diretamente pela linha de comando ou IDE:

```bash
mvn spring-boot:run
```


** 3. Deploy Local via Docker **

Utilize o script automatizado que gerencia o build da imagem e a execução do container:

```bash
./deploy.bat

```


** 4. Carga de Dados Inicial **

Crie os dados iniciais (Perfis, Permissões e Usuário Admin) utilizando os scripts SQL ou componentes de bootstrap da aplicação.

** 📖 Documentação da API (Endpoints) **

O projeto expõe a documentação interativa através do Swagger/OpenAPI:

```
👉 Swagger UI: http://localhost:8080/swagger-ui/index.html
👉 OpenAPI JSON: http://localhost:8080/v3/api-docs
👉 OpenAPI YAML: http://localhost:8080/v3/api-docs.yaml

```

** 🔒 Segurança **
A API utiliza autenticação Stateless via JWT.

Os endpoints de /auth (login/register) são públicos.

Os demais endpoints exigem o Header Authorization: Bearer <TOKEN>.

O controle de acesso é baseado em perfis (Roles) carregados do banco de dados.

