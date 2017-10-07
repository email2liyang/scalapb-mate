package io.datanerd.securemsg.service

import java.util.concurrent.Executors

import com.google.inject.{Inject, Singleton}
import io.datanerd.generated.securemsg._
import io.datanerd.securemsg.dao.MessageDao
import io.datanerd.securemsg.guice.PowerConfig
import io.grpc.Status
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Future

@Singleton
class MessageServiceImpl @Inject()(config: PowerConfig, messageDao: MessageDao) extends MessageServiceGrpc.MessageService {

  private val log: Logger = LoggerFactory.getLogger(this.getClass)
  // see more from https://www.beyondthelines.net/computing/scala-future-and-execution-context/
  implicit val executor = scala.concurrent.ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

  override def postMessage(request: SecureMsg): Future[PostResult] = {
    log.info("calling post message")
    messageDao.saveSecureMsg(request)
  }

  override def getMessage(request: MessageId): Future[SecureMsgResult] = {
    messageDao.getMessage(request.messageId)
      .flatMap {
        case Some(secureMsg) => Future.successful(SecureMsgResult(secureMsg.encryptedData))
        case None => Future.failed(Status.NOT_FOUND.asRuntimeException())
      }
  }
}
