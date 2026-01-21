FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
# Busca o jar dinamicamente para evitar erro de nome
COPY --from=build /app/target/*.jar app.jar

ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport"
# Adicionada a variável para o Spring reconhecer o profile do Docker
ENV SPRING_PROFILES_ACTIVE=docker

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
