# Learning Kafka Producer / Consumer

## Documentation

https://kafka.apache.org/documentation/

Imagen alternativa

* https://github.com/bitnami/bitnami-docker-kafka

## Commands

start the services

```.bash
- docker-compose -f ./single-broker.yml up -d
```

stop the services

```.bash
- docker-compose -f ./single-broker.yml stop
```

show the logs

```shell
- docker-compose -f ./single-broker.yml logs -f
```

list a topic

```shell
docker exec -it kafka-prod-cons_kafka_1 /bin/bash 
/opt/kafka_2.12-2.3.0/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
```

create a topic

```shell
/opt/kafka_2.12-2.3.0/bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 8 --topic json
```

delete a topic

```shell
/opt/kafka_2.12-2.3.0/bin/kafka-topics.sh  --bootstrap-server localhost:9092 --delete --topic test1
```

describe a topic
```shell
/opt/kafka_2.12-2.3.0/bin/kafka-topics.sh --describe --bootstrap-server localhost:9092 --topic json
```

consume messages from the beginning
```shell
/opt/kafka_2.12-2.3.0/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic json
```

consume messages of a partition from the beginning
```shell
/opt/kafka_2.12-2.3.0/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092  --topic json --offset earliest --partition 1 
```

## Docker Images

- https://github.com/simplesteph/kafka-stack-docker-compose/blob/master/zk-single-kafka-multiple.yml

- https://hub.docker.com/r/wurstmeister/kafka/

- https://github.com/wurstmeister/kafka-docker

- https://hub.docker.com/r/sheepkiller/kafka-manager

- https://github.com/yahoo/kafka-manager

- https://hub.docker.com/r/wurstmeister/kafka/