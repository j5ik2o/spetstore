import sbt._
import Keys._
import play.Project._

object SpetstoreBuild extends Build {

  val appName = "spetstore"
  val appVersion = "1.0.0"

  val appDependencies = Seq(
    jdbc,
        "org.scala-lang"          % "scala-reflect"               % "2.10.3",
        "junit"                   % "junit"                       % "4.8.1" % "test",
        "org.hamcrest"            % "hamcrest-all"                % "1.3" % "test",
        "org.mockito"             % "mockito-core"                % "1.9.5" % "test",
        "org.specs2"              %% "specs2"                     % "2.0" % "test",
        "net.codingwell"          %% "scala-guice"                % "4.0.0-beta",
        "com.github.nscala-time"  %% "nscala-time"                % "0.6.0",
        "org.scalikejdbc"         %% "scalikejdbc"                % "[1.7,)",
        "org.scalikejdbc"         %% "scalikejdbc-interpolation"  % "[1.7,)",
        "org.scalikejdbc"         %% "scalikejdbc-play-plugin"    % "[1.7,)",
        "com.h2database"          %  "h2"                         % "[1.3,)",
        "ch.qos.logback"          %  "logback-classic"            % "[1.0,)",
        "org.json4s"              %% "json4s-ext"                 % "3.2.4",
        "org.json4s"              %% "json4s-jackson"             % "3.2.4",
        "com.github.tototoshi"    %% "play-json4s-jackson"        % "0.2.0",
        "com.github.tototoshi"    %% "play-json4s-test-jackson"   % "0.2.0" % "test",
        "com.github.tototoshi"    %% "play-json4s-native"         % "0.1.0",
        "com.github.tototoshi"    %% "play-flyway"                % "0.1.4"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
      scalaVersion in ThisBuild := "2.10.3",
      conflictWarning := ConflictWarning.disable
    )
/*
  lazy val spetstore = play.Project(
    id = "spetstore",
    base = file("."),
    settings = Project.defaultSettings ++ playScalaSettings ++ Seq(
      name := "spetstore",
      organization := "com.j5ik2o.spetstore",
      version := "0.0.1",
      scalaVersion := "2.10.3",
      scalacOptions ++= Seq("-encoding", "UTF-8", "-feature", "-deprecation", "-unchecked"),
      javacOptions ++= Seq("-encoding", "UTF-8", "-deprecation"),
      resolvers ++= Seq(
        "Typesafe Repository"           at "http://repo.typesafe.com/typesafe/releases/",
        "Sonatype Release Repository"   at "https://oss.sonatype.org/content/repositories/releases/",
        "Sonatype Snapshot Repository"  at "https://oss.sonatype.org/content/repositories/snapshots/"
      ),
      libraryDependencies ++= Seq(
        "org.scala-lang"          % "scala-reflect"               % "2.10.3",
        "junit"                   % "junit"                       % "4.8.1" % "test",
        "org.hamcrest"            % "hamcrest-all"                % "1.3" % "test",
        "org.mockito"             % "mockito-core"                % "1.9.5" % "test",
        "org.specs2"              %% "specs2"                     % "2.0" % "test",
        "net.codingwell"          %% "scala-guice"                % "4.0.0-beta",
        "com.github.nscala-time"  %% "nscala-time"                % "0.6.0",
        "org.scalikejdbc"         %% "scalikejdbc"                % "[1.7,)",
        "org.scalikejdbc"         %% "scalikejdbc-interpolation"  % "[1.7,)",
        "com.h2database"          %  "h2"                         % "[1.3,)",
        "ch.qos.logback"          %  "logback-classic"            % "[1.0,)",
        "org.json4s"              %% "json4s-jackson"             % "3.2.6"
      )
    )
  )
  */
}
