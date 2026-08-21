
FROM eclipse-temurin:26-jdk-alpine

WORKDIR /app

COPY target/ATMSimulator-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xmx256m", "-jar", "app.jar"]