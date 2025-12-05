# Estágio de Build (usando uma imagem Java com Maven)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
# Copia o arquivo de configuração do projeto (POM) e baixa dependências
COPY pom.xml .
RUN mvn dependency:go-offline
# Copia todo o código-fonte
COPY src ./src
# Constrói o JAR final (ignora testes para ser mais rápido no deploy)
RUN mvn clean install -DskipTests

# Estágio de Execução (usando uma imagem Java menor e mais segura)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copia o JAR do estágio de build para o estágio de execução
# O nome do JAR deve ser ajustado se for diferente de 'veiculos-0.0.1-SNAPSHOT.jar'
COPY --from=build /app/target/veiculos-0.0.1-SNAPSHOT.jar veiculos-app.jar
# Comando para rodar a aplicação quando o contêiner iniciar
ENTRYPOINT ["java", "-jar", "veiculos-app.jar"]