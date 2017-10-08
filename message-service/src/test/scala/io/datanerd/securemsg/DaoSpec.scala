package io.datanerd.securemsg

import com.dimafeng.testcontainers.{ForAllTestContainer, GenericContainer}
import com.github.javafaker.Faker
import io.datanerd.securemsg.guice.PowerConfig
import org.scalatest.FlatSpec
import org.slf4j.{Logger, LoggerFactory}
import org.testcontainers.containers.wait.Wait

class DaoSpec extends FlatSpec with ForAllTestContainer {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)
  val faker: Faker = new Faker()
  override val container = GenericContainer(
    "mongo:latest",
    exposedPorts = Seq(27017),
    waitStrategy = Wait.forListeningPort()
  )

  //docker run --rm -p 27017:27017 --name mongo mongo
  override def afterStart(): Unit = {
    //setup docker mongo attribute
    PowerConfig.enableMemConfig()
    PowerConfig.overrideMemory("config.resource", "application-test.conf")

    val host = container.containerIpAddress
    val port = container.mappedPort(27017).toString
    val dbName = faker.numerify("test-db-####")
    PowerConfig.overrideMemory("message-service.mongo.uri", s"mongodb://${host}:${port}/${dbName}")
    PowerConfig.overrideMemory("message-service.mongo.dbName", dbName)
  }
}
