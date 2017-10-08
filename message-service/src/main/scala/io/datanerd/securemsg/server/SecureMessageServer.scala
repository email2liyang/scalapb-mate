package io.datanerd.securemsg.server

import com.google.inject.{Inject, Singleton}
import io.datanerd.generated.securemsg.MessageServiceGrpc
import io.datanerd.generated.securemsg.MessageServiceGrpc.MessageService
import io.datanerd.securemsg.guice.PowerConfig
import io.grpc.netty.NettyServerBuilder

import scala.concurrent.ExecutionContext

@Singleton
class SecureMessageServer @Inject()(config: PowerConfig, messageService: MessageService) {

  def start() = {
    NettyServerBuilder
      .forPort(config.getString("message-service.port").toInt)
      .addService(MessageServiceGrpc.bindService(messageService, ExecutionContext.global))
      .build()
      .start()
  }
}
