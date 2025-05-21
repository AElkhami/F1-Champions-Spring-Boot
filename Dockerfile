FROM openjdk:17-jdk-slim

WORKDIR /app

# Stage 1 - Build
FROM gradle:8.3-jdk17 AS builder
COPY . /home/app
WORKDIR /home/app
RUN ./gradlew build -x test

# Stage 2 - Run
FROM openjdk:17-jdk-slim
COPY --from=builder /home/app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]


ENTRYPOINT ["java", "-jar", "app.jar"]
