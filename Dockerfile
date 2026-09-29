# Берем сверхстабильную JDK 21
FROM docker.io/library/eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Копируем встроенный Gradle Wrapper
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle.kts settings.gradle.kts ./

# Лечим проблему переносов строк винды
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

# Копируем исходный код
COPY src ./src

# Собираем приложение
RUN ./gradlew bootJar -x test --no-daemon

# Берем легкую JRE 21 для запуска
FROM docker.io/library/eclipse-temurin:21-jre-alpine AS runner
WORKDIR /app

# Копируем собранный JAR-файл
COPY --from=builder /app/build/libs/*.jar app.jar

# Открываем порт
EXPOSE 8080

# Точка входа
ENTRYPOINT ["java", "-jar", "app.jar"]