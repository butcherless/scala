[![Build Status](https://semaphoreci.com/api/v1/butcherless/scala/branches/master/badge.svg)](https://semaphoreci.com/butcherless/scala)

[![CircleCI](https://circleci.com/gh/butcherless/scala.svg?style=svg)](https://circleci.com/gh/butcherless/scala)

[![Run Status](https://api.shippable.com/projects/5b68c6d5e815be060012a3c5/badge?branch=master)](https://app.shippable.com/github/butcherless/scala)

[![Build Status](https://travis-ci.org/butcherless/scala.svg?branch=master)](https://travis-ci.org/butcherless/scala)

[![Coverage Status](https://coveralls.io/repos/github/butcherless/scala/badge.svg?branch=master)](https://coveralls.io/github/butcherless/scala?branch=master)

[![codecov](https://codecov.io/gh/butcherless/scala/branch/master/graph/badge.svg)](https://codecov.io/gh/butcherless/scala)

[![](https://codescene.io/projects/3185/status.svg) Get more details at **codescene.io**.](https://codescene.io/projects/3185/jobs/latest-successful/results)

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
- Overview of free monad in cats: https://blog.scalac.io/2016/06/02/overview-of-free-monad-in-cats.html
- Json support: https://doc.akka.io/docs/akka-http/current/common/json-support.html

## Create basic project script
```.env
bash <(curl -s https://raw.githubusercontent.com/butcherless/scala/master/docs/create-simple-project.sh)
```
 
## Test & Coverage report
- testOnly TestSuite -- -z CURRENT
- command: sbt clean coverage test coverageReport
- report: target/scala-2.13/scoverage-report/index.html
- check dependencies: sbt dependencyBrowseTree

## Plugins
- https://github.com/jrudolph/sbt-dependency-graph
- https://github.com/sbt/sbt-assembly/releases