FROM ubuntu:20.04 AS build

RUN apt-get update
RUN apt-get install -y openjdk-17-jdk

COPY . .

RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon

FROM openjdk:17-alpine

COPY --from=build /build/libs/service-advertisement-0.0.1.jar /app/service-advertisement-0.0.1.jar

ENTRYPOINT ["java", "-jar", "app/service-advertisement-0.0.1.jar"]