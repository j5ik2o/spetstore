name := "spetstore"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.5"

resolvers += "Sonatype OSS Release Repository" at "https://oss.sonatype.org/content/repositories/releases/"
resolvers += "Bintary JCenter" at "http://jcenter.bintray.com"

val circeVersion = "0.7.0"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  filters,
  "org.scalatestplus.play"  %% "scalatestplus-play"             % "2.0.0-M1"  % Test,
  "org.scala-lang"          % "scala-reflect"                   % scalaVersion.value,
  "org.hamcrest"            % "hamcrest-all"                    % "1.3"       % Test,
  "org.mockito"             % "mockito-core"                    % "1.10.19"   % Test,
  "org.specs2"              %% "specs2-core"                    % "3.8.9"     % Test,
  "org.specs2"              %% "specs2-junit"                   % "3.8.9"     % Test,
  "net.codingwell"          %% "scala-guice"                    % "4.1.0",
  "com.github.nscala-time"  %% "nscala-time"                    % "2.16.0",
  "org.scalikejdbc"         %% "scalikejdbc"                    % "2.5.1",
  "org.scalikejdbc"         %% "scalikejdbc-test"               % "2.5.1",
  "org.scalikejdbc"         %% "scalikejdbc-config"             % "2.5.1",
  "org.scalikejdbc"         %% "scalikejdbc-play-initializer"   % "2.5.+",
  "org.scalikejdbc"         %% "scalikejdbc-play-dbapi-adapter" % "2.5.+",
  "org.skinny-framework"    %% "skinny-orm"                     % "2.3.5",
  "com.h2database"          %  "h2"                             % "1.4.187",
//  "ch.qos.logback"          %  "logback-classic"                % "1.2.1",
  //"io.swagger" %% "swagger-play2" % "1.5.1",
  "org.flywaydb"            %% "flyway-play"                    % "3.0.1",
  "mysql"                   % "mysql-connector-java"            % "6.0.5",
  "io.circe"                %% "circe-core"                     % circeVersion,
  "io.circe"                %% "circe-generic"                  % circeVersion,
  "io.circe"                %% "circe-parser"                   % circeVersion,
  "play-circe" %% "play-circe" % "2.5-0.7.0"
)

routesGenerator := InjectedRoutesGenerator

scalariformSettings

flywayUrl := "jdbc:mysql://localhost/spetstore?autoReconnect=true&useSSL=false"

flywayUser := "spetstore"

flywayPassword := "phou8Igh"

flywayLocations := Seq("filesystem:conf/db/migration/default")


