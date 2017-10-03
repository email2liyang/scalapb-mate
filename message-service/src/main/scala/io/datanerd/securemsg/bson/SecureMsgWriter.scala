package io.datanerd.securemsg.bson

import io.datanerd.generated.securemsg.SecureMsg
import reactivemongo.bson.{BSONDocument, BSONDocumentWriter, _}

object SecureMsgWriter extends BSONDocumentWriter[SecureMsg] {

  override def write(secureMsg: SecureMsg): BSONDocument = {
    BSONDocument(
      "encryptedData" -> secureMsg.encryptedData,
      "durationHours" -> secureMsg.durationHours,
      "userPass" -> secureMsg.userPass,
      "notifyEmail" -> secureMsg.notifyEmail
    )
  }
}
