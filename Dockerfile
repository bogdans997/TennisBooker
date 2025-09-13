# syntax=docker/dockerfile:1

# ---- Build stage ----
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -B -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -B -DskipTests package

# ---- Run stage ----
FROM eclipse-temurin:21-jre
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"
WORKDIR /app
# copy the fat jar into the image as app.jar
COPY --from=build /app/target/*.jar /app/app.jar
# directory for mounted Firebase secret
RUN mkdir -p /run/secrets/firebase
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE:-docker}"]
