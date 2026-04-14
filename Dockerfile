FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

COPY --from=build /app/target/dms-backend-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java","-Xmx350m","-Xss256k","-XX:+UseContainerSupport","-jar","/app.jar"]