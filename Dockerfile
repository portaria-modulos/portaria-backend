# Estágio 1: Build da aplicação
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /portaria

# Copia o POM separadamente para otimizar o cache de dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia apenas o código-fonte (protegido pelo .dockerignore)
COPY src ./src

# Executa o empacotamento limpo ignorando testes
RUN mvn clean package -DskipTests

# Estágio 2: Imagem final enxuta
FROM eclipse-temurin:17-jre-focal
WORKDIR /portaria

# Copia o jar gerado no estágio anterior
COPY --from=build /portaria/target/portaria-0.0.1-SNAPSHOT.jar portaria.jar

EXPOSE 8080
ENV DATA_DIR=/var/lib/data

CMD ["java", "-jar", "portaria.jar"]