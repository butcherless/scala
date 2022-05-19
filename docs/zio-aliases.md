# Aliases for ZIO effect

    ZIO[R,E,A]
    R -> enviRonment
    E -> error
    A -> computation type

| [R]&#8595; -- [E]&#8594; | Unfailable | Throwable | Error      |
| ------------------------ | ---------- | --------- | ---------- |
| **no enviRonment**       | UIO[A]     | Task[A]   | IO[E,A]    |
| **enviRonment**          | URIO[R,A]  | RIO[R,A]  | ZIO[R,E,A] |

| [R]&#8595; -- [E]&#8594; | Unfailable         | Throwable            | Error        |
| ------------------------ | ------------------ | -------------------- | ------------ |
| **no enviRonment**       | ZIO[Any,Nothing,A] | ZIO[Any,Throwable,A] | ZIO[Any,E,A] |
| **enviRonment**          | ZIO[R,Nothing,A]   | ZIO[R,Throwable,A]   | ZIO[R,E,A]   |
