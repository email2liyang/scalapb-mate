package io.datanerd.securemsg.server

import grpcgateway.server.GrpcGatewayServerBuilder
import io.datanerd.generated.securemsg.MessageServiceHandler
import io.grpc.ManagedChannelBuilder
import scala.concurrent.ExecutionContext.Implicits.global

object GatewayLauncher extends App{
  val channel = ManagedChannelBuilder.forAddress("localhost",8080)
    .usePlaintext(true)
    .build()
  val gateway = GrpcGatewayServerBuilder
    .forPort(8090)
    .addService(new MessageServiceHandler(channel))
    .build()

  gateway.start()

  sys.addShutdownHook {
    gateway.shutdown()
  }

}
