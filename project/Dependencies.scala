import sbt._

object Dependencies {
  object mysql {
    val mysqlConnectorJava = "mysql" % "mysql-connector-java" % "5.1.42"
  }
  object megard {
    val akkaHttpCors =   "ch.megard"                    %% "akka-http-cors"    % "0.3.0"
  }
  object github {
    val swaggerAkkaHttp = "com.github.swagger-akka-http" %% "swagger-akka-http" % "2.0.0"
  }
  object javax {
    val rsApi = "javax.ws.rs"                  % "javax.ws.rs-api"    % "2.0.1"
  }
  object circe {
    val circeVersion = "0.10.0-M1"
    val circeCore = "io.circe" %% "circe-core" % circeVersion
    val circeGeneric = "io.circe" %% "circe-generic" % circeVersion
    val circeParser = "io.circe" %% "circe-parser" % circeVersion
  }
  object akka {
    val akkaHttpVersion = "10.1.5"
    val akkaVersion = "2.5.12"
    val akkaHttp   = "com.typesafe.akka" %% "akka-http"   % akkaHttpVersion
    val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
  }
  object slick {
    val slickVersion  = "3.2.0"
    val slick         = "com.typesafe.slick" %% "slick" % slickVersion
    val slickHikaricp = "com.typesafe.slick" %% "slick-hikaricp" % slickVersion
  }
  object typelevel {
    val catsCore = "org.typelevel" %% "cats-core" % "1.1.0"
    val catsFree = "org.typelevel" %% "cats-free" % "1.1.0"
  }
  object beachape {
    val version = "1.5.13"
    val enumeratum = "com.beachape" %% "enumeratum" % version
    val enumeratumCirce = "com.beachape" %% "enumeratum-circe" % version
  }
  object monix {
    val monix = "io.monix" %% "monix" % "3.0.0-RC1"
  }
  object j5ik2o {
    val scalaTestPlusDb = "com.github.j5ik2o" %% "scalatestplus-db" % "1.0.5"
    val scalaDDDBaseCore = "com.github.j5ik2o" %% "scala-ddd-base-core"  % "1.0.11"
    val scalaDDDBaseSlick = "com.github.j5ik2o" %% "scala-ddd-base-slick" % "1.0.11"
  }
  object sisioh {
    val baseunitsScala = "org.sisioh" %% "baseunits-scala" % "0.1.21"
  }
  object t3hnar {
    val scalaBcrpyt = "com.github.t3hnar" %% "scala-bcrypt"         % "3.1"
  }
  object hashids {
    val hashids = "org.hashids" % "hashids" % "1.0.3"
  }
  object scalatest {
    val scalatest = "org.scalatest"        %% "scalatest" % "3.0.1"
  }
  object airframe {
    val airframe = "org.wvlet.airframe"   %% "airframe" % "0.68"
  }
  object heikoseeberger {
    val akkaHttpCirce = "de.heikoseeberger" %% "akka-http-circe" % "1.20.0-RC2"
  }
}
