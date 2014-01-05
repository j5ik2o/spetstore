import sbt._
import sbt.Keys._

object SpetstoreBuild extends Build {

  lazy val spetstore = Project(
    id = "spetstore",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
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
}
