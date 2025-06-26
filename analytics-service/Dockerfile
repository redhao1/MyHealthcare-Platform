FROM openjdk:17-jdk-slim AS runner

WORKDIR /app

COPY target/analytics-service-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the port your Spring Boot app runs on (default 8080)
EXPOSE 4003

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]