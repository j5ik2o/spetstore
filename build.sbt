name := "spetstore"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.5"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
  jdbc,
  "org.scala-lang"          % "scala-reflect"               % "2.11.5",
  "org.hamcrest"            % "hamcrest-all"                % "1.3" % "test",
  "org.mockito"             % "mockito-core"                % "1.10.19" % "test",
  "org.specs2"              %% "specs2-core"                % "3.2" % "test",
  "net.codingwell"          %% "scala-guice"                % "4.0.0-beta5",
  "com.github.nscala-time"  %% "nscala-time"                % "1.8.0",
  "org.scalikejdbc"         %% "scalikejdbc"                % "2.2.5",
  "org.scalikejdbc"         %% "scalikejdbc-config"                % "2.2.5",
  "org.scalikejdbc"         %% "scalikejdbc-play-plugin"           % "2.3.6",
  "org.scalikejdbc"         %% "scalikejdbc-play-fixture-plugin"   % "2.3.6",
  "org.skinny-framework"    %% "skinny-orm"                 % "1.3.16",
  "com.h2database"          %  "h2"                         % "1.4.187",
  "ch.qos.logback"          %  "logback-classic"            % "[1.1,)",
  "org.json4s"              %% "json4s-ext"                 % "3.2.11",
  "org.json4s"              %% "json4s-jackson"             % "3.2.11",
  "com.github.tototoshi"    %% "play-json4s-jackson"        % "0.3.1",
  "com.github.tototoshi"    %% "play-json4s-test-jackson"   % "0.3.1" % "test",
  "com.github.tototoshi"    %% "play-json4s-native"         % "0.3.1",
  "com.github.tototoshi"    %% "play-flyway"                % "1.2.1",
  "mysql"                   %  "mysql-connector-java"       % "5.1.35"
)


