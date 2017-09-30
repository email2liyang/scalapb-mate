package io.datanerd.securemsg

import org.scalatest._
import org.slf4j.LoggerFactory

class UnitSpec extends FlatSpec with Matchers with OptionValues with Inside with Inspectors with BeforeAndAfterEach{
  val logger = LoggerFactory.getLogger(this.getClass)
  override def beforeEach() {
    logger.info("before each")
    System.setProperty("config.resource", "application-test.conf")
  }

  override def afterEach() {
    logger.info("after each")
    System.clearProperty("config.resource")
  }
}
