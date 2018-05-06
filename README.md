[![Build Status](https://semaphoreci.com/api/v1/butcherless/scala/branches/master/badge.svg)](https://semaphoreci.com/butcherless/scala)

# At the beginning of a long road ...

## Training material:
- Programming in Scala, Martin Odersky & Lex Spoon
- Functional Programming in Scala, Paul Chiusano & Rúnar Bjarnason
- Programming Scala: Scalability = Functional Programming + Objects, Dean Wampler & Alex Payne
- Scala in Depth, Joshua D. Suereth
- http://www.hablapps.com/#functional-101
- http://www.hablapps.com/#functional-advanced
- https://blog.hablapps.com/2016/01/22/from-hello-world-to-hello-monad-part-i/
- https://github.com/hablapps/gist/tree/master/src/test/scala/hello-monads
- https://blog.hablapps.com/2017/06/26/functional-apis-an-oop-approach-to-fp/
- flatMap hell: https://www.reddit.com/r/scala/comments/75de27/concrete_examples_of_mapflatmap_solve_callback/
- free monad: https://underscore.io/blog/posts/2015/04/14/free-monads-are-simple.html

## Create basic project script
- bash script @ ${PROJECT_ROOT}/docs directory
- bash <(curl -s https://raw.githubusercontent.com/butcherless/scala/master/docs/create-simple-project.sh)
 
## Coverage report
- sbt ";clean;coverage;test;coverageReport"
