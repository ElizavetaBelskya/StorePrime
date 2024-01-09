mvn clean install -DskipTests
docker build -t market_app_image .
docker-compose up