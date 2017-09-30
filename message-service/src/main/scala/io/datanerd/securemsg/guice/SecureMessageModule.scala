package io.datanerd.securemsg.guice

import com.google.inject.{AbstractModule,Singleton}
import com.typesafe.config.{Config, ConfigFactory}
import io.datanerd.generated.securemsg.MessageServiceGrpc
import io.datanerd.securemsg.service.MessageServiceImpl
import net.codingwell.scalaguice.ScalaModule

class SecureMessageModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[Config].toInstance(ConfigFactory.load())
    bind[MessageServiceGrpc.MessageService].to[MessageServiceImpl].in[Singleton]
  }
}
