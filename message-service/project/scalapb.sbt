addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.9")

resolvers += Resolver.bintrayRepo("beyondthelines", "maven")

libraryDependencies += "com.trueaccord.scalapb" %% "compilerplugin" % "0.6.3"
libraryDependencies += "beyondthelines" %% "grpcgatewaygenerator" % "0.0.4"
