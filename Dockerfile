FROM adoptopenjdk/maven-openjdk11 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:11-jdk-slim

WORKDIR /app

COPY --from=build /app/target/event-api-service-0.0.1.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]
