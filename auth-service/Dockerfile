FROM maven:3.9.14-eclipse-temurin-25-alpine AS builder

WORKDIR /build

COPY pom.xml .

RUN mvn -B -q -e -C -DskipTests dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
