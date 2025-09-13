FROM openjdk:17

WORKDIR /app

COPY target/weather-service-0.0.1-SNAPSHOT.jar /app/
COPY src/main/resources/application.properties /app/

CMD ["java", "-jar", "weather-service-0.0.1-SNAPSHOT.jar"]