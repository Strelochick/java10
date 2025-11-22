# Используем официальный образ OpenJDK 17
FROM eclipse-temurin:17-jdk-jammy

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файл pom.xml и скачиваем зависимости (кеширование слоев)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Даем права на выполнение для Maven wrapper
RUN chmod +x mvnw

# Устанавливаем Maven зависимости
RUN ./mvnw dependency:go-offline -B

# Копируем исходный код
COPY src ./src

# Собираем приложение
RUN ./mvnw clean package -DskipTests

# Открываем порт (Railway автоматически установит переменную PORT)
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "target/journal-0.0.1-SNAPSHOT.jar"]

