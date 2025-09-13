FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy the built jar from target folder
COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
