# 1. Estágio de Build: Usa o Maven para compilar o código
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
# Copia o arquivo de dependências
COPY pom.xml .
# Baixa as dependências (cache)
RUN mvn dependency:go-offline
# Copia o código fonte e gera o .jar
COPY src ./src
RUN mvn clean package -DskipTests

# 2. Estágio de Execução: Cria uma imagem leve apenas com o Java
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copia apenas o arquivo compilado do estágio anterior
COPY --from=build /app/target/*.jar app.jar
# Porta que sua API usa
EXPOSE 8082
# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]