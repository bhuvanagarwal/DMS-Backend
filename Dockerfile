FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-Xmx350m","-Xss256k","-XX:+UseContainerSupport","-jar","/app.jar"]