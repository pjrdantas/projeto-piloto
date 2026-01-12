# Etapa 1: Build do Maven (Otimizada com Cache)
FROM maven:3.9.0-eclipse-temurin-17 as build
WORKDIR /app

# 1. Copia apenas o pom e baixa dependências (Cache Layer)
COPY pom.xml .
RUN mvn dependency:go-offline

# 2. Agora copia o código e builda (Se mudar o código, não baixa as dependências de novo)
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final (Segura e Leve)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Adiciona um usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copia o JAR da etapa de build
COPY --from=build /app/target/*.jar app.jar

# Configurações de JVM para rodar melhor em containers
ENV JAVA_OPTS="-Xmx512m -Xms256m"

EXPOSE 8080

# ENTRYPOINT flexível para aceitar parâmetros de memória
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
