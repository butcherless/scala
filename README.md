[![Build Status](https://semaphoreci.com/api/v1/butcherless/scala/branches/master/badge.svg)](https://semaphoreci.com/butcherless/scala)

# At the beginning of a long road...

## Training books:
 - Programming in Scala, Martin Odersky & Lex Spoon
 - Functional Programming in Scala, Paul Chiusano & Rúnar Bjarnason
 - Programming Scala: Scalability = Functional Programming + Objects, Dean Wampler & Alex Payne
 - Scala in Depth, Joshua D. Suereth
 - http://www.hablapps.com/#functional-101
 - http://www.hablapps.com/#functional-advanced

## Create basic project script
- bash script @ ${PROJECT_ROOT}/docs directory
- bash <(curl -s https://raw.githubusercontent.com/butcherless/scala/master/docs/create-simple-project.sh)
 
## Coverage report

 - sbt ";clean;coverage;test;coverageReport"

