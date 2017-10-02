package io.datanerd.securemsg.dao

import com.google.inject.Inject
import io.datanerd.securemsg.guice.MongoDbName
import org.slf4j.{Logger, LoggerFactory}
import reactivemongo.api.MongoConnection
import reactivemongo.api.collections.bson.BSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class MessageDao @Inject()(connection: MongoConnection, @MongoDbName dbName: String) {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

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
}
