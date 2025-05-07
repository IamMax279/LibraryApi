FROM maven:latest AS build

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/MaxLibrary-0.0.1-SNAPSHOT.jar /app/target/MaxLibrary-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/target/MaxLibrary-0.0.1-SNAPSHOT.jar"]