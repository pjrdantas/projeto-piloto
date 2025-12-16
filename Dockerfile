# Etapa 1: Build do Maven
FROM maven:3.9.0-eclipse-temurin-17 as build

# Diretório de trabalho para o Maven
WORKDIR /app

# Copiar o pom.xml e o arquivo de código fonte para o container
COPY pom.xml .
COPY src ./src

# Rodar o comando Maven para construir o arquivo .jar
RUN mvn clean install -DskipTests

# Etapa 2: Imagem final
FROM eclipse-temurin:17-jdk-alpine

# Diretório de trabalho para a aplicação
WORKDIR /app

# Copiar o arquivo .jar gerado para o novo container
COPY --from=build /app/target/projeto-piloto-0.0.1-SNAPSHOT.jar app.jar

# Expor a porta que a aplicação vai rodar
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
