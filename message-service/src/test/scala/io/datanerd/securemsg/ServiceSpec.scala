package io.datanerd.securemsg

import io.datanerd.securemsg.guice.PowerConfig
import org.scalatest._
import org.scalatest.mockito.MockitoSugar
import org.slf4j.LoggerFactory

trait ServiceSpec extends FlatSpec with BeforeAndAfterEach with MockitoSugar {

  val logger = LoggerFactory.getLogger(this.getClass)

  override def beforeEach() {
    PowerConfig.enableMemConfig()
  }

}
