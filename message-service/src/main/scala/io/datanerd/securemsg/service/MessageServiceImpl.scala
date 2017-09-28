package io.datanerd.securemsg.service

import java.util.concurrent.Executors

import io.datanerd.generated.securemsg._

import scala.concurrent.Future

class MessageServiceImpl extends MessageServiceGrpc.MessageService {

  // see more from https://www.beyondthelines.net/computing/scala-future-and-execution-context/
  implicit val executor = scala.concurrent.ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

  override def postMessage(request: SecureMsg): Future[PostResult] = {
    Future.successful(PostResult().withMessageUrl("x"))
  }

  override def getMessage(request: MessageId): Future[SecureMsgResult] = {
    Future.successful(SecureMsgResult().withEncryptedData("data"))
  }
}
