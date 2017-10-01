package io.datanerd.securemsg.dao

import com.google.inject.{Guice, Injector}
import io.datanerd.securemsg.DaoSpec
import io.datanerd.securemsg.guice.SecureMessageModule
import net.codingwell.scalaguice.InjectorExtensions._
import org.assertj.core.api.Assertions

class MessageDaoTest extends DaoSpec {

  def withParams(testCode: Injector => Any) = {
    val injector = Guice.createInjector(new SecureMessageModule())
    testCode(injector)
  }

  "isConnected" should "be true" in withParams { injector =>
    val messageDao = injector.instance[MessageDao]

    Assertions.assertThat(messageDao.isConnected).isTrue
  }

}
