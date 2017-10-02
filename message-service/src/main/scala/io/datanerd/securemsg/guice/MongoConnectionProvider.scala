package io.datanerd.securemsg.guice

import com.google.inject.{Inject, Provider}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteConcern
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.api.{MongoConnection, MongoConnectionOptions, MongoDriver, ReadPreference}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class MongoConnectionProvider @Inject()(config: PowerConfig, @MongoDbName dbName: String) extends Provider[MongoConnection] {

  override def get(): MongoConnection = {
    val host = config.getString("message-service.mongo.host")
    val port = config.getString("message-service.mongo.port")

    val conOpts = MongoConnectionOptions(
      writeConcern = WriteConcern.Acknowledged,
      readPreference = ReadPreference.primaryPreferred
    )
    val mongoDriver = new MongoDriver
    val connection = mongoDriver.connection(List(s"$host:$port"), options = conOpts)
    createMessageDbIndex(connection)

    connection
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
