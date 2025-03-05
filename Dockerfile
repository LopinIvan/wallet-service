FROM openjdk:17-slim

WORKDIR /app

COPY target/wallet-service-0.0.1-SNAPSHOT.jar /app/wallet-service.jar

EXPOSE 8080

CMD ["java", "-Dspring.liquibase.enabled=true", "-jar", "/app/wallet-service.jar"]