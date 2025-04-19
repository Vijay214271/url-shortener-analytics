# Use OpenJDK runtime only (no Maven)
FROM openjdk:17-jdk-slim

# Create app directory
WORKDIR /app

# Copy your built jar from host into the container
COPY target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
