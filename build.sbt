val enumeratumVersion = "1.5.13"
val circeVersion = "0.9.3"

val commonSettings = Seq(
  scalaVersion := "2.12.6",
  organization := "com.github.j5ik2o.spetstore",
  version := "1.0.0-SNAPSHOT",
  libraryDependencies ++= Seq(
    "com.beachape" %% "enumeratum" % enumeratumVersion,
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion
  ),
  scalafmtOnCompile in ThisBuild := true,
  scalafmtTestOnCompile in ThisBuild := true,
)

lazy val infrastructure = (project in file("infrastructure")).settings(commonSettings).settings(
  name := "spetstore-infrastructure"
)

lazy val domain = (project in file("domain")).settings(commonSettings).settings(
  name := "spetstore-domain",
  libraryDependencies ++= Seq(
    "com.github.j5ik2o" %% "scala-ddd-base-core" % "1.0.12"
  )
).dependsOn(infrastructure)

lazy val api = (project in file("api")).settings(commonSettings).settings(
  name := "spetstore-api",
  libraryDependencies ++= Seq(
    guice,
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
    "io.swagger" %% "swagger-play2" % "1.6.0",
    // "org.webjars" % "swagger-ui" % "2.2.0"
  )
  // Adds additional packages into Twirl
  //TwirlKeys.templateImports += "com.github.j5ik2o.spetstore.controllers._"
  // Adds additional packages into conf/routes
  // play.sbt.routes.RoutesKeys.routesImport += "com.github.j5ik2o.spetstore.binders._"
).dependsOn(domain).enablePlugins(PlayScala)

lazy val root = (project in file(".")).settings(commonSettings).aggregate(api)



