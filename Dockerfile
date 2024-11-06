FROM maven:3.9.9-amazoncorretto-21-alpine AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn install -DskipTests=true


FROM alpine:3.20

RUN apk add --no-cache openjdk21-jre

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
