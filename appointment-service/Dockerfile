# BUILD
# Use a lightweight OpenJDK 17 runtime image as builder
#FROM maven:3.8.6-eclipse-temurin-17 AS builder
#
## Set working directory inside the container
#WORKDIR /app
#
## Copy the parent POM, POM in this module and source folder
## into corresponding directory of container
#COPY pom.xml .
#RUN mvn dependency:go-offline -B
#COPY src ./src
#
## Install the parent POM
## Fetch dependencies for current module
#
## Build current module
#RUN mvn package
#RUN mvn clean install


FROM openjdk:17-jdk-slim AS runner

WORKDIR /app

COPY target/appointment-service-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the port your Spring Boot app runs on (default 8080)
EXPOSE 4006

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
