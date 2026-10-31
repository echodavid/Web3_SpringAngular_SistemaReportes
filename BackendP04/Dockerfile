FROM maven:3.9.9-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src src
RUN mvn -B package
FROM eclipse-temurin:11-jre-jammy
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build /app/target/reportes-ciudadanos-1.0.0.jar app.jar
USER 10001
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
