# =========================
# Etapa 1: Build Maven (com cache)
# =========================
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app

# Copia apenas o pom para cache de dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código e faz o build
COPY src ./src
RUN mvn clean package -DskipTests

# =========================
# Etapa 2: Imagem final (leve e segura)
# =========================
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Adiciona usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copia o JAR da etapa de build
COPY --from=build /app/target/*.jar app.jar

# Configurações de JVM para container
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Expõe a porta do Spring Boot
EXPOSE 8080

# ENTRYPOINT flexível, aceita variáveis de ambiente
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
