# Learning Kafka Producer / Consumer

## Documentation

https://kafka.apache.org/documentation/

## Commands

Run these from this directory, `compose.yml` is picked up automatically.

start the services

```shell
docker compose up -d
```

stop the services

```shell
docker compose stop
```

show the logs

```shell
docker compose logs -f
```

list the topics

```shell
docker compose exec kafka kafka-topics --bootstrap-server localhost:9092 --list
```

create a topic

```shell
docker compose exec kafka kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 8 --topic json
```

delete a topic

```shell
docker compose exec kafka kafka-topics --bootstrap-server localhost:9092 --delete --topic test1
```

describe a topic

```shell
docker compose exec kafka kafka-topics --describe --bootstrap-server localhost:9092 --topic json
```

consume messages from the beginning

```shell
docker compose exec kafka kafka-console-consumer --bootstrap-server localhost:9092 --from-beginning --topic json
```

consume messages of a partition from the beginning

```shell
docker compose exec kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic json --offset earliest --partition 1
```

## Docker Images

- https://hub.docker.com/r/confluentinc/cp-kafka
