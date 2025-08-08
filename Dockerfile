# ---- BUILD STAGE ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

# ---- RUNTIME STAGE ----
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/ecom-website-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
