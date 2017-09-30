package io.datanerd.securemsg.service

import java.util.concurrent.Executors

import com.google.inject.{Inject, Singleton}
import com.typesafe.config.Config
import io.datanerd.generated.securemsg._
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Future

@Singleton
class MessageServiceImpl @Inject() (config:Config) extends MessageServiceGrpc.MessageService {

  private val log: Logger = LoggerFactory.getLogger(this.getClass)
  // see more from https://www.beyondthelines.net/computing/scala-future-and-execution-context/
  implicit val executor = scala.concurrent.ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

  override def postMessage(request: SecureMsg): Future[PostResult] = {
    log.info("calling post message")
    Future.successful(PostResult().withMessageUrl("x"))
  }

  override def getMessage(request: MessageId): Future[SecureMsgResult] = {
    Future.successful(SecureMsgResult().withEncryptedData("data"))
  }
}
