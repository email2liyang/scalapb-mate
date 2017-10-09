package io.datanerd.securemsg.server

import com.google.inject.Guice
import io.datanerd.securemsg.guice.SecureMessageModule

object GrpcServerLauncher extends App {

  Guice.createInjector(new SecureMessageModule())
    .getInstance(classOf[SecureMessageServer])
    .start()
}
