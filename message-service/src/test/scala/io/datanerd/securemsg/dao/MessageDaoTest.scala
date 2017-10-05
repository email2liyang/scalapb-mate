package io.datanerd.securemsg.dao

import com.google.inject.{Guice, Injector}
import io.datanerd.generated.securemsg.SecureMsg
import io.datanerd.securemsg.DaoSpec
import io.datanerd.securemsg.guice.SecureMessageModule
import net.codingwell.scalaguice.InjectorExtensions._
import org.assertj.core.api.Assertions

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class MessageDaoTest extends DaoSpec {

  def withParams(testCode: Injector => Any) = {
    val injector = Guice.createInjector(new SecureMessageModule())
    testCode(injector)
  }

  "isConnected" should "be true" in withParams { injector =>
    val messageDao = injector.instance[MessageDao]

    Assertions.assertThat(messageDao.isConnected).isTrue
  }

  "saveSecureMsg" should "save to db" in withParams { injector =>
    val messageDao = injector.instance[MessageDao]

    val secureMsg = new SecureMsg()
      .withEncryptedData(faker.superhero().name())
      .withDurationHours(1)
      .withUserPass("pass")
      .withNotifyEmail("email2liyang@gmail.com")

    val postResultFuture = messageDao.saveSecureMsg(secureMsg)
    postResultFuture.onComplete {
      case Success(postResult) => {
        Assertions.assertThat(postResult).isNotNull
        logger.info("========={}",postResult)
      }
      case Failure(e) => Assertions.fail("failed to save secure msg", e)
    }

    Await.result(postResultFuture, 5.seconds)

  }

}
