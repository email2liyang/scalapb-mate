package io.datanerd.securemsg.guice

import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Singleton}
import com.typesafe.config.{Config, ConfigFactory}
import io.datanerd.generated.securemsg.MessageServiceGrpc
import io.datanerd.securemsg.service.MessageServiceImpl
import net.codingwell.scalaguice.ScalaModule
import reactivemongo.api.MongoConnection

class SecureMessageModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    val config = ConfigFactory.load()
    bind[Config].toInstance(config)
    bind[MessageServiceGrpc.MessageService].to[MessageServiceImpl].in[Singleton]
    bind[String].annotatedWith(Names.named("dbName")).toInstance(config.getString("message-service.mongo.dbName"))
    bind[MongoConnection].toProvider[MongoConnectionProvider].in[Singleton]
  }
}
