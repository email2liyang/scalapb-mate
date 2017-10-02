package io.datanerd.securemsg

import io.datanerd.securemsg.guice.PowerConfig
import org.scalatest._
import org.slf4j.LoggerFactory

class ServiceSpec extends FlatSpec with BeforeAndAfterEach {

  val logger = LoggerFactory.getLogger(this.getClass)

  override def beforeEach() {
    logger.info("before each")
    PowerConfig.enableMemConfig()
  }

  override def afterEach(): Unit = {
    logger.info("after each")
  }
}
