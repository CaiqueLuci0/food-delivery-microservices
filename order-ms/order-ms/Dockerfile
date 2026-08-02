FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src ./src

RUN chmod +x mvnw && ./mvnw -q -DskipTests package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN addgroup -S app && adduser -S app -G app
USER app

COPY --from=build /app/target/order-ms-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8004

ENTRYPOINT ["java", "-jar", "app.jar"]
