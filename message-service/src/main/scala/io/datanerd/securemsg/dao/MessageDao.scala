package io.datanerd.securemsg.dao

import com.google.inject.{Inject, Singleton}
import io.datanerd.generated.securemsg.{PostResult, SecureMsg}
import io.datanerd.securemsg.bson.SecureMsgWriter
import io.datanerd.securemsg.guice.MongoDbName
import io.datanerd.securemsg.service.Encryption
import org.slf4j.{Logger, LoggerFactory}
import reactivemongo.api.MongoConnection
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONObjectID

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Random

@Singleton
class MessageDao @Inject()(connection: MongoConnection, @MongoDbName dbName: String) {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  private implicit val secureMsgWriter = SecureMsgWriter

  def getCollection(): Future[BSONCollection] = {
    connection.database(dbName).map(_.collection("message"))
  }

  def isConnected: Boolean = {
    //make sure index is available
    Await
      .result(
        getCollection().flatMap(_.indexesManager.list()),
        1.second
      )
      .nonEmpty
  }

  def saveSecureMsg(secureMsg: SecureMsg): Future[PostResult] = {
    val messageId = BSONObjectID.generate.stringify
    secureMsg.withMessageId(messageId)

    getCollection()
      .flatMap(_.insert(secureMsg.withMessageId(messageId)))
      .map(_ => PostResult(generateMessageUrl(messageId)))
  }

  def generateMessageUrl(messageId: String): String = {
    val key = Random.alphanumeric.take(10).mkString
    val encryptedId = Encryption.encrypt(key,messageId)

    return s"${encryptedId}#${key}"
  }
}
