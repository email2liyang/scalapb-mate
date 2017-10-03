package io.datanerd.securemsg.bson

import io.datanerd.generated.securemsg.SecureMsg
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, _}

object SecureMsgReader extends BSONDocumentReader[SecureMsg] {

  override def read(bson: BSONDocument): SecureMsg = {
    val opt: Option[SecureMsg] = for {
      messageId <- bson.getAs[String]("_id")
      encryptedData <- bson.getAs[String]("encryptedData")
      durationHours <- bson.getAs[BSONNumberLike]("durationHours").map(_.toInt)
      userPass <- bson.getAs[String]("userPass")
      notifyEmail <- bson.getAs[String]("notifyEmail")
    } yield {
      new SecureMsg(messageId, encryptedData, durationHours, userPass, notifyEmail)
    }

    opt.get // the person is required (or let throw an exception)
  }
}
