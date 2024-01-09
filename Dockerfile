
FROM openjdk:17-alpine
COPY target/StorePrime-1-1.0.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/app.jar"]




