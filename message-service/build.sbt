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
      // compile your proto files into scala source files
      scalapb.gen(grpc = true, flatPackage = true) -> (sourceManaged in Compile).value,
//      // generate Swagger spec files into the `resources/specs`
      grpcgateway.generators.SwaggerGenerator -> (resourceDirectory in Compile).value / "specs",
//      // generate the Rest Gateway source code
      grpcgateway.generators.GatewayGenerator -> (sourceManaged in Compile).value
    ),
    resolvers += Resolver.bintrayRepo("beyondthelines", "maven"),
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.3.1",
      "net.codingwell" %% "scala-guice" % "4.1.0",
      "org.reactivemongo" %% "reactivemongo" % "0.12.6",
      "io.grpc" % "grpc-netty" % "1.6.1",
      "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % "0.6.3",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "org.scalatest" %% "scalatest" % "3.0.1" % "test",
      "org.pegdown" % "pegdown" % "1.6.0" % "test",
      "org.assertj" % "assertj-core" % "3.8.0" % "test",
      "org.mockito" % "mockito-core" % "2.10.0" % "test",
      "com.github.javafaker" % "javafaker" % "0.13" % "test",
      "com.dimafeng" %% "testcontainers-scala" % "0.7.0" % "test",
      "com.trueaccord.scalapb" %% "scalapb-runtime" % "0.6.3" % "protobuf",
      "beyondthelines" %% "grpcgatewayruntime" % "0.0.4" % "compile,protobuf"
    ),
    testOptions in Test ++= Seq(
      Tests.Argument(TestFrameworks.ScalaTest, "-o"),
      Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports")
    )
  )


