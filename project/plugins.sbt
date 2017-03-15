// Comment to get more information during initialization
logLevel := Level.Warn

resolvers += Resolver.typesafeRepo("releases")
resolvers += "Flyway" at "https://flywaydb.org/repo"

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.10")

// web plugins
addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.1.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.4")

addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.8")

addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-mocha" % "1.1.0")

addSbtPlugin("org.irundaia.sbt" % "sbt-sassify" % "1.4.6")

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.6.0")

addSbtPlugin("org.flywaydb" % "flyway-sbt" % "4.0.3")

