# scala/akka http exercises

## How to run and check

- sbt
- project akkahttphw
- run
- Test CRUD
  - Create: http -v POST localhost:8080/transfer < src/test/resources/transfer-dto.json
  - Read: http -v GET localhost:8080/transfer/4e4387c4-38e0-4fd8-80cd-2ca7a6395d8e
  - Update: http -v PUT localhost:8080/transfer/4e4387c4-38e0-4fd8-80cd-2ca7a6395d8e < src/test/resources/transfer.json 
  - Delete: http -v DELETE localhost:8080/transfer/4e4387c4-38e0-4fd8-80cd-2ca7a6395d8e
 

## Links

- https://doc.akka.io/docs/akka-http/current/index.html
