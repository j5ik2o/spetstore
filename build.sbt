val commonSettings = Seq(
  scalaVersion := "2.12.6",
  organization := "com.github.j5ik2o.spetstore",
  version := "1.0.0-SNAPSHOT"
)

lazy val domain = (project in file("domain")).settings(commonSettings).settings(
  name := "spetstore-domain"
)

lazy val api = (project in file("api")).settings(commonSettings).settings(
  name := "spetstore-api",
  libraryDependencies ++= Seq(
    guice,
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
    "io.swagger" %% "swagger-play2" % "1.6.0"
  )
  // Adds additional packages into Twirl
  //TwirlKeys.templateImports += "com.github.j5ik2o.spetstore.controllers._"
  // Adds additional packages into conf/routes
  // play.sbt.routes.RoutesKeys.routesImport += "com.github.j5ik2o.spetstore.binders._"
).dependsOn(domain).enablePlugins(PlayScala)

lazy val root = (project in file(".")).settings(commonSettings).aggregate(api)



