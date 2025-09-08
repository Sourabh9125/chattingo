# Stage 1
FROM eclipse-temurin:17-jdk-alpine AS build 

WORKDIR /app

COPY pom.xml ./

COPY . .
RUN ./mvnw dependency:go-offline

RUN ./mvnw clean package -DskipTests=true

# Stage 2
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

RUN addgroup --system user && adduser --system --ingroup user user
USER user

ENV SPRING_PROFILES_ACTIVE=development
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/chattingo_db?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false

CMD ["java", "-jar", "app.jar"]
