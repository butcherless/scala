# Akka Actors Subproject Notes

Migration to ZIO

- Akka Actor Future <-> ZIO Fiber
- graceful shutdown => zio.ensuring{...} or zio.acquireRelease{...}

| Akka   | ZIO.      |
|--------|-----------|
| Source | ZStream   |
| Flow   | ZPipeline |
| Sink   | ZSink     |


Actors:

- Dispatcher actor
- Worker actor

Model:


Messages:

- Request(number: Int)
- Accepted(number: Int)
- Processed(number: Int)
- Rejected(number: Int, reason: Reason)

Message States:

- Created
- Send
- Resend
- Accepted
- Processed
- Rejected

Use cases:

_UC-1_

1. Dispatcher Actor (DA) receives a message, n = random number, from a Source[Int]
2. DA generates a random number, p = position, to assign task to a Worker Actor (WA)
3. DA registers the message (number)
4. DA creates a Request object with message information an position assigned
5. DA stores the Request Id with Created state [Log]
6. DA send the Request message to the Worker at position (p)
7. DA updates Request state to Send [Log]
8.  WA sends Accepted message to DA
9.  DA updates message state to Accepted [Log]
10. WA process the message
11. WA sends Processed message to DA
12. DA updates message state to Processed [Log]
13. DA removes the Request for the processed message [Log]


**Helpers**

- generate an infinite Source of Integers
- 

---

number of times the message was rejected: hop

register number: UUID, LocalDateTime, requestId

calculate modulo

generate random position
