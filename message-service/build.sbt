lazy val commonSettings = Seq(
  organization := "io.datanerd.securemsg",
  version := "0.1.0",
  scalaVersion := "2.12.3"
)

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    name := "message-service",
    PB.targets in Compile := Seq(
      scalapb.gen(grpc = true, flatPackage = true) -> (sourceManaged in Compile).value
    ),
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.3.1",
      "net.codingwell" %% "scala-guice" % "4.1.0",
      "io.grpc" % "grpc-netty" % "1.6.1",
      "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % "0.6.6",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "org.scalatest" %% "scalatest" % "3.0.1" % "test",
      "org.assertj" % "assertj-core" % "3.8.0" % "test",
      "org.mockito" % "mockito-core" % "2.10.0" % "test",
      "com.github.javafaker" % "javafaker" % "0.13" % "test",
      "org.testcontainers" % "testcontainers" % "1.4.2" % "test",
      "com.trueaccord.scalapb" %% "scalapb-runtime" % "0.6.6" % "protobuf"
    )
  )


