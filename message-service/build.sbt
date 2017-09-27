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
      "io.grpc" % "grpc-netty" % "1.6.1",
      "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % "0.6.6",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "org.scalatest" %% "scalatest" % "3.0.1" % "test",
      "com.trueaccord.scalapb" %% "scalapb-runtime" % "0.6.6" % "protobuf"
    )
  )

