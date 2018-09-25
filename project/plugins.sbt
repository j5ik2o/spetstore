resolvers += "Sonatype OSS Release Repository" at "https://oss.sonatype.org/content/repositories/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.19")

addSbtPlugin("com.lucidchart" % "sbt-scalafmt" % "1.15")

addSbtPlugin("io.github.davidmweber" % "flyway-sbt" % "5.0.0")

addSbtPlugin("com.chatwork" % "sbt-wix-embedded-mysql" % "1.0.9")

addSbtPlugin("jp.co.septeni-original" % "sbt-dao-generator" % "1.0.8")