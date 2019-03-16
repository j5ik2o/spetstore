resolvers ++= Seq(
  "Sonatype OSS Snapshot Repository" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype OSS Release Repository" at "https://oss.sonatype.org/content/repositories/releases/",
  "Seasar Repository" at "http://maven.seasar.org/maven2/"
)

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.19")

addSbtPlugin("com.lucidchart" % "sbt-scalafmt" % "1.15")

addSbtPlugin("com.chatwork" % "sbt-wix-embedded-mysql" % "1.0.11")

addSbtPlugin("jp.co.septeni-original" % "sbt-dao-generator" % "1.0.8")

addSbtPlugin("io.github.davidmweber" % "flyway-sbt" % "5.0.0")