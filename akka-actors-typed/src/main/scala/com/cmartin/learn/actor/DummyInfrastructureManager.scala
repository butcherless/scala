package com.cmartin.learn.actor

object DummyInfrastructureManager {
  val KAFKA_AGENT      = "kafka-health-agent"
  val POSTGRESQL_AGENT = "postgres-db-health-agent"
  val SYSTEM_AGENT     = "system-health-agent"

  def getKafkaStatus(): String =
    """
      |{
      |  "kafka": "kafka cluster is alive"
      |}
      |""".stripMargin

  def getPostgresqlStatus(): String =
    """
      |{
      |  "postgresql": "postgresql database running"
      |}
      |""".stripMargin

  def getSystemStatus(): String =
    """
      |{
      |  "memory": 512,
      |  "cores": 2
      |}
      |""".stripMargin
}
