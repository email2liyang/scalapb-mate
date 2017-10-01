package io.datanerd.securemsg.guice

import com.google.inject.{Inject, Provider}
import com.typesafe.config.Config
import reactivemongo.api.commands.WriteConcern
import reactivemongo.api.{MongoConnection, MongoConnectionOptions, MongoDriver, ReadPreference}

class MongoConnectionProvider @Inject()(config: Config) extends Provider[MongoConnection] {

  override def get(): MongoConnection = {
    val host = config.getString("message-service.mongo.host")
    val port = config.getInt("message-service.mongo.port")

    val conOpts = MongoConnectionOptions(
      writeConcern = WriteConcern.Acknowledged,
      readPreference = ReadPreference.primaryPreferred
    )
    val mongoDriver = new MongoDriver
    mongoDriver.connection(List(s"$host:$port"),options = conOpts)
  }
}
