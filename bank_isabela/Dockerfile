
FROM amazoncorretto:21-alpine as builder

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080 3306

ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=docker"]
