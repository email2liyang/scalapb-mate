package io.datanerd.securemsg

import com.dimafeng.testcontainers.{ForAllTestContainer, GenericContainer}
import com.github.javafaker.Faker
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
    System.setProperty("config.resource", "application-test.conf")
    System.setProperty("message-service.mongo.host", container.containerIpAddress)
    System.setProperty("message-service.mongo.port", container.mappedPort(27017).toString)
    System.setProperty("message-service.mongo.dbName", "local")
  }

  override def beforeStop(): Unit = {
    System.clearProperty("config.resource")
    System.clearProperty("message-service.mongo.host")
    System.clearProperty("message-service.mongo.port")
    System.clearProperty("message-service.mongo.dbName")
  }
}
