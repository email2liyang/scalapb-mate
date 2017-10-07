package io.datanerd.securemsg.service

import com.github.javafaker.Faker
import com.google.inject.Guice
import com.google.inject.util.Modules
import io.datanerd.generated.securemsg.MessageServiceGrpc.MessageServiceBlockingStub
import io.datanerd.generated.securemsg.{MessageId, MessageServiceGrpc, PostResult, SecureMsg}
import io.datanerd.securemsg.ServiceSpec
import io.datanerd.securemsg.dao.MessageDao
import io.datanerd.securemsg.guice.SecureMessageModule
import io.grpc.StatusRuntimeException
import io.grpc.inprocess.{InProcessChannelBuilder, InProcessServerBuilder}
import org.assertj.core.api.Assertions
import org.mockito.Mockito

import scala.concurrent.{ExecutionContext, Future}

class MessageServiceImplTest extends ServiceSpec {

  val faker = new Faker()

  def withParams(testCode: (MessageServiceBlockingStub, MessageDao) => Any) = {
    import net.codingwell.scalaguice.InjectorExtensions._
    val messageDao = mock[MessageDao]
    val injector = Guice.createInjector(
      Modules.`override`(new SecureMessageModule())
        .`with`(binder => binder.bind(classOf[MessageDao]).toInstance(messageDao))
    )
    val messageService: MessageServiceGrpc.MessageService = injector.instance[MessageServiceGrpc.MessageService]
    val serverName = faker.numerify("server-####")

    val grpcServer = InProcessServerBuilder
      .forName(serverName)
      .addService(MessageServiceGrpc.bindService(messageService, ExecutionContext.global))
      .build()
    val channel = InProcessChannelBuilder.forName(serverName).usePlaintext(true).build()
    val blockingStub = MessageServiceGrpc.blockingStub(channel)
    grpcServer.start()

    testCode(blockingStub, messageDao)

    channel.shutdown()
    grpcServer.shutdown()
  }

  "get message" should "return data when dao return data" in withParams { (stub, messageDao) =>
    Mockito.when(messageDao.getMessage(org.mockito.ArgumentMatchers.anyString()))
      .thenReturn(Future.successful(Some(SecureMsg().withEncryptedData("data"))))
    val secureMessageResult = stub.getMessage(new MessageId().withMessageId(faker.numerify("id-#")))
    Assertions.assertThat(secureMessageResult.encryptedData).isEqualTo("data")
  }

  it should "report error when dao return none" in withParams { (stub, messageDao) =>
    Mockito.when(messageDao.getMessage(org.mockito.ArgumentMatchers.anyString()))
      .thenReturn(Future.successful(None))
    assertThrows[StatusRuntimeException] {
      stub.getMessage(new MessageId().withMessageId(faker.numerify("id-#")))
    }
  }

  "postMessage" should "return x" in withParams { (stub, messageDao) =>

    Mockito.when(messageDao.saveSecureMsg(org.mockito.ArgumentMatchers.any()))
      .thenReturn(Future.successful(PostResult("x")))

    val postResult = stub.postMessage(new SecureMsg())
    Assertions.assertThat(postResult.messageUrl).isEqualTo("x")

  }

}
