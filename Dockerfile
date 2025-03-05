# Используем образ OpenJDK для выполнения приложения
FROM openjdk:17-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR файл из локальной машины
COPY target/wallet-service-0.0.1-SNAPSHOT.jar /app/wallet-service.jar

# Открываем порт 8080
EXPOSE 8080

# Запускаем приложение, выполняем миграции Liquibase при старте
CMD ["java", "-Dspring.liquibase.enabled=true", "-jar", "/app/wallet-service.jar"]