lazy val commonSettings = Seq(
  organization := "io.datanerd.securemsg",
  version := "0.1.0",
  scalaVersion := "2.12.3"
)

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    name := "message-service",
    libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )
