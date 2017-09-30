package io.datanerd.securemsg.guice

import com.google.inject.{Guice, Injector}
import com.typesafe.config.Config
import io.datanerd.securemsg.UnitSpec
import org.assertj.core.api.Assertions._
import org.scalatest.BeforeAndAfterEach

class SecureMessageModuleTest extends UnitSpec with BeforeAndAfterEach {

  def getInjector(): Injector = Guice.createInjector(new SecureMessageModule)

  override def beforeEach() {
    logger.info("before each")
    System.setProperty("config.resource", "application-test.conf")
  }

  override def afterEach() {
    logger.info("after each")
    System.clearProperty("config.resource")
  }

  "A config file" should "override by unit test " in {
    import net.codingwell.scalaguice.InjectorExtensions._
    val config: Config = getInjector().instance[Config]
    assertThat(config.getString("message-service.testKey")).isNotNull
    assertThat(config.getString("message-service.testKey")) isEqualTo "key"
  }

}
