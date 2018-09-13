resolvers ++= Seq(
  "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/",
  "Sonatype OSS Snapshot Repository" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype OSS Release Repository" at "https://oss.sonatype.org/content/repositories/releases/",
  "Seasar Repository" at "http://maven.seasar.org/maven2/",
  "Flyway" at "https://davidmweber.github.io/flyway-sbt.repo"
)

libraryDependencies ++= Seq(
  "com.h2database"  % "h2"         % "1.4.195",
  "commons-io"      % "commons-io" % "2.5",
  "org.seasar.util" % "s2util"     % "0.0.1"
)

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.18")

//// web plugins
//addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.0")
//
//addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.1.0")
//
//addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.4")
//
//addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.8")
//
//addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.1")
//
//addSbtPlugin("com.typesafe.sbt" % "sbt-mocha" % "1.1.0")
//
//addSbtPlugin("org.irundaia.sbt" % "sbt-sassify" % "1.4.6")
//
addSbtPlugin("com.lucidchart" % "sbt-scalafmt" % "1.15")

addSbtPlugin("org.flywaydb" % "flyway-sbt" % "4.2.0")

