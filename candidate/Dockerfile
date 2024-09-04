# Build Stage
FROM maven:3.9.8 AS build

WORKDIR /build

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

################################################################################

# Exec Stage
FROM eclipse-temurin:17-jre-ubi9-minimal AS exec

WORKDIR /opt/app

COPY --from=build /build/target/candidate*.jar ./candidate.jar

ARG UID=10001

RUN useradd \
    --uid "${UID}" \
    --home-dir "/nonexistent" \
    --shell "/sbin/nologin" \
    appuser

USER appuser

EXPOSE 8080 

ENTRYPOINT ["java","-jar","./candidate.jar"]