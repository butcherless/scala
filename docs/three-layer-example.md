# Three layer application example

## Sequence diagram

Typical application:
- Domain layer
- Service layer (Use cases)
- REST API layer
- Repository layer
- Messaging layer
- Logging layer
- Tracing layer
- Metrics layer
- Configuratior layer

Side effects:
- Domain & Use cases (NO, in memory)
- Infrastructure (YES, file, network, database, topic, queue, socket)

## Axioms
- change sentences: do => describe
- program as computation sequence
- compiler
- effect system
- interpreter

## Implementation problems
- error management
- error accumulation
- resource management (acquisition, release)
- cancelation
- logging
- tracing
- metrics
- crosscutting concerns (aspects)
- validation
- dependency injection
- encoder / decoder
