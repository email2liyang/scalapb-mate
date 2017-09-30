package io.datanerd.securemsg.service

import com.github.javafaker.Faker
import com.google.inject.Guice
import io.datanerd.generated.securemsg.{MessageId, MessageServiceGrpc, SecureMsg}
import io.datanerd.generated.securemsg.MessageServiceGrpc.{MessageService, MessageServiceBlockingStub}
import io.datanerd.securemsg.UnitSpec
import io.datanerd.securemsg.guice.SecureMessageModule
import io.grpc.inprocess.{InProcessChannelBuilder, InProcessServerBuilder}
import org.assertj.core.api.Assertions

import scala.concurrent.ExecutionContext

class MessageServiceImplTest extends UnitSpec {
  val faker = new Faker()
  def withParams(testCode: MessageServiceBlockingStub => Any) = {
    import net.codingwell.scalaguice.InjectorExtensions._
    val injector = Guice.createInjector(new SecureMessageModule())
    val messageService:MessageServiceGrpc.MessageService = injector.instance[MessageServiceGrpc.MessageService]
    val serverName = faker.numerify("server-####")

    val grpcServer =InProcessServerBuilder
      .forName(serverName)
      .addService(MessageServiceGrpc.bindService(messageService,ExecutionContext.global))
      .build()
    val channel = InProcessChannelBuilder.forName(serverName).usePlaintext(true).build()
    val blockingStub = MessageServiceGrpc.blockingStub(channel)
    grpcServer.start()

    testCode(blockingStub)

    channel.shutdown()
    grpcServer.shutdown()
  }

  "get message " should " return data" in withParams { stub=>
    val secureMessageResult = stub.getMessage(new MessageId().withMessageId(faker.numerify("id-#")))
    Assertions.assertThat(secureMessageResult.encryptedData).isEqualTo("data")
  }

  "postMessage" should " return x" in withParams { stub =>

    val postResult = stub.postMessage(new SecureMsg())
    Assertions.assertThat(postResult.messageUrl).isEqualTo("x")

  }

}
