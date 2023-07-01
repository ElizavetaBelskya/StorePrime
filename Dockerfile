
FROM openjdk:17-alpine
COPY target/market_jar/Market.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/app.jar"]




