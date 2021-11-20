# Akka Typed Actors notes


Goal: restrict `ActorRef` to sending the right message. Tell operator accepts only the right message:

- add a type parameter: `ActorRef[T]`
- only allow sending messages of type `T` with `tell` or `!`
- add type of understood messages to trait `Actor` (1)
- add type of understood messages to `ActorContext[T]`

Remember Actor actions:

1. send a finite number of messages
2. create a finite number of actors
3. designate the behavior for the next message. A function from incoming message => next behavior


Changes from non-typed Actor:

- turn stateful trait `Actor` into pure `Behaviour[T]`. Behaviour of T describe the reaction of the Actor to an incoming message. Behavior is a function from incoming message to next behavior of the actor.
- guardian behavior, remove system.actorOf
- `ActorSystem[T]` is an ActorRef[T] for the guardian

In akka typed we don't create an actor, we create its behavior

Modeling protocols with algebraic data types. Message types represent the sequence of events.
- lecture 5.2, https://courses.edx.org/courses/course-v1:EPFLx+scala-reactiveX+2T2019/courseware/edd30f5c234b430e85700f42dba9abf8/a543c10238454b3dad7d04139b8adf89/?child=first


Akka Typed Facilities

- Akka Typed Adapters. Speaking multiple protocols. An actor needs to speak with multiple parties. `A <-> B <-> C`. Actor needs protocol between `A` and `C`.
- Child Actors for protocol sessions. Alternative to Adapters.
- Defer handling a message. Stash messages.
- Type-safe service discovery service registry at each ActorSystem.




