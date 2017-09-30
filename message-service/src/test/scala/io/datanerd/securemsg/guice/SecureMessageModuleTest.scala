package io.datanerd.securemsg.guice

import com.google.inject.{Guice, Injector}
import com.typesafe.config.Config
import io.datanerd.securemsg.UnitSpec
import org.assertj.core.api.Assertions._
import org.scalatest.BeforeAndAfterEach

class SecureMessageModuleTest extends UnitSpec with BeforeAndAfterEach {

  override def beforeEach() {
    logger.info("before each")
    System.setProperty("config.resource", "application-test.conf")
  }

  override def afterEach() {
    logger.info("after each")
    System.clearProperty("config.resource")
  }

  def withInjector(testCode: Injector => Any): Unit = {
    val injector = Guice.createInjector(new SecureMessageModule)
    testCode(injector)
  }

  "A config file" should "override by unit test " in withInjector { injector =>
    import net.codingwell.scalaguice.InjectorExtensions._
    val config: Config = injector.instance[Config]
    assertThat(config.getString("message-service.testKey")).isNotNull
    assertThat(config.getString("message-service.testKey")) isEqualTo "key"
  }

}
