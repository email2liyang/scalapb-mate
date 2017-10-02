package io.datanerd.securemsg.guice

import com.google.inject.{Guice, Injector}
import com.typesafe.config.Config
import io.datanerd.securemsg.ServiceSpec
import org.assertj.core.api.Assertions._

class SecureMessageModuleTest extends ServiceSpec  {

  def withInjector(testCode: Injector => Any): Unit = {
    val injector = Guice.createInjector(new SecureMessageModule)
    testCode(injector)
  }

  "A config file" should "override by unit test " in withInjector { injector =>
    import net.codingwell.scalaguice.InjectorExtensions._
    val config: PowerConfig = injector.instance[PowerConfig]
    assertThat(config.getString("message-service.mongo.dbName")).isNotNull
    assertThat(config.getString("message-service.mongo.dbName")) isEqualTo "message"
  }

}
