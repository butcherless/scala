# Learning Kafka Producer / Consumer

## Documentation

https://kafka.apache.org/documentation/

Imagen alternativa

* https://github.com/bitnami/bitnami-docker-kafka

## Commands

start services

```.bash
- docker-compose -f ./single-broker.yml up -d
```

stop services

```.bash
- docker-compose -f ./single-broker.yml stop
```

show logs

```.bash
- docker-compose -f ./single-broker.yml logs -f
```

list topic

```.bash
docker exec -it kafka-prod-cons_kafka_1 /bin/bash
/opt/kafka_2.12-2.3.0/bin/kafka-topics.sh --list --bootstrap-server localhost:909
```

create topic

```.bash
/opt/kafka_2.12-2.3.0/bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 8 --topic test1
```

delete topic

```.bash
/opt/kafka_2.12-2.3.0/bin/kafka-topics.sh  --bootstrap-server localhost:9092 --delete --topic test1
```

## Docker Images

- https://github.com/simplesteph/kafka-stack-docker-compose/blob/master/zk-single-kafka-multiple.yml

- https://hub.docker.com/r/wurstmeister/kafka/

- https://github.com/wurstmeister/kafka-docker

- https://hub.docker.com/r/sheepkiller/kafka-manager

- https://github.com/yahoo/kafka-manager

- https://hub.docker.com/r/wurstmeister/kafka/