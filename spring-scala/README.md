[![Build Status](https://travis-ci.org/butcherless/scala.svg?branch=master)](https://travis-ci.org/butcherless/scala/spring-scala)

# scala/spring integration learning

Build with Travis CI

## How to run and check

- build: ./gradlew clean build
- run: ./gradlew bootRun
- http http://localhost:8080/
- http http://localhost:8080/greeting/
- http http://localhost:8080/greeting/?name=Donald
- http http://localhost:8080/randomWord/7
- http DELETE localhost:8080/delete/b08a6e8c-9d48-419a-a14c-29d34fc1c49d
- http http://localhost:8080/api/v1/person
- Test CRUD
  - Create: echo '{"name": "Donald"}' | http -v POST http://localhost:8080/api/v1/person
  - Create: http -v POST localhost:8080/create < src/test/resources/person.json
  - Read: http -v http://localhost:8080/greeting/?name=Donald
  - Update: http -v PUT http://localhost:8080/api/v1/person/185 nombre=Thief-u 
  - Delete: http -v DELETE http://localhost:8080/api/v1/person/666
 

## Links

- https://docs.spring.io/spring-data/neo4j/docs/current/reference/html/
