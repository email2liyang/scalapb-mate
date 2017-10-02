package io.datanerd.securemsg.guice

import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters._
import scala.collection.mutable

class PowerConfig {

  val fileConfig: Config = ConfigFactory.load()

  def getString(key: String): String = {
    PowerConfig.memConfigAllowed match {
      case Some(_) => PowerConfig.memConfig.getOrElse(key, fileConfig.getString(key))
      case None => fileConfig.getString(key)
    }
  }
}

object PowerConfig {

  val memConfig: mutable.Map[String, String] = new java.util.concurrent.ConcurrentHashMap[String, String].asScala
  var memConfigAllowed: Option[Boolean] = None

  def enableMemConfig = memConfigAllowed = Some(true)

  def disableMemConfig = memConfigAllowed = None

  def overrideMemory(key: String, value: String) = {
    PowerConfig.memConfigAllowed match {
      case Some(_) => memConfig.put(key, value)
      case None => throw new IllegalStateException("unit-test, pls call PowerConfig.enableMemConfig")
    }
  }
}
