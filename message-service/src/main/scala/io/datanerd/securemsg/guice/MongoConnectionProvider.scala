package io.datanerd.securemsg.guice

import com.google.inject.{Inject, Provider}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.api.{MongoConnection, MongoDriver}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

class MongoConnectionProvider @Inject()(config: PowerConfig, @MongoDbName dbName: String) extends Provider[MongoConnection] {
  
  override def get(): MongoConnection = {
    val mongoDriver = new MongoDriver
    val mongoUri = config.getString("message-service.mongo.uri")

    MongoConnection.parseURI(mongoUri).map { parsedUri =>
      mongoDriver.connection(parsedUri)
    } match {
      case Success(connection) =>
        createMessageDbIndex(connection)
        connection
      case Failure(exception) =>
        throw new IllegalStateException("error on mongo connection", exception)
    }
  }

  def createMessageDbIndex(connection: MongoConnection) = {
    val messageColl: Future[BSONCollection] = connection.database(dbName)
      .map(_.collection("message"))

    val indexOps: Future[List[Boolean]] = messageColl
      .map(_.indexesManager)
      .map(indexManager => {
        List(
          indexManager.ensure(Index(List(("_id", IndexType.Ascending)))),
          indexManager.ensure(Index(List(("message_url", IndexType.Ascending)))),
        )
      })
      .flatMap(Future.sequence(_))

    Await.result(indexOps, 10.seconds)
  }
}
