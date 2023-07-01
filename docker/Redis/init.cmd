docker pull redis
docker build -t market_redis .
docker volume create --name market_redis_volume
