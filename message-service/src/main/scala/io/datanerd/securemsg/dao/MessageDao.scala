package io.datanerd.securemsg.dao

import com.google.inject.Inject
import com.google.inject.name.Named
import reactivemongo.api.MongoConnection
import reactivemongo.api.collections.bson.BSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class MessageDao @Inject()(connection: MongoConnection, @Named("dbName") dbName: String) {

  import org.slf4j.{Logger, LoggerFactory}

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  def getCollection(): Future[BSONCollection] = {
    connection.database(dbName).map(_.collection("startup_log"))
  }

  def isConnected: Boolean = {
    Await.result(getCollection().flatMap(_.count()), Duration.Inf) > 0
  }
}
