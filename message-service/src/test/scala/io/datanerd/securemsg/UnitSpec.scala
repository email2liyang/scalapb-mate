package io.datanerd.securemsg

import org.scalatest._
import org.slf4j.LoggerFactory

class UnitSpec extends FlatSpec with Matchers with OptionValues with Inside with Inspectors{
  val logger = LoggerFactory.getLogger(this.getClass)
}
