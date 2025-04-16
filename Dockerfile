FROM openjdk:22-jdk-slim

MAINTAINER bnfd

ARG SPRING_PROFILES_ACTIVE=docker
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}

COPY target/overseer.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
