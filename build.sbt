import org.seasar.util.lang.StringUtil

import scala.concurrent.duration._

val enumeratumVersion = "1.5.13"
val circeVersion = "0.9.3"

val commonSettings = Seq(
  scalaVersion := "2.12.6",
  organization := "com.github.j5ik2o.spetstore",
  version := "1.0.0-SNAPSHOT",
  libraryDependencies ++= Seq(
    "com.beachape" %% "enumeratum" % enumeratumVersion,
    "com.beachape" %% "enumeratum-circe" % enumeratumVersion,
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "org.sisioh" %% "baseunits-scala" % "0.1.21"
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
    "org.wvlet.airframe" %% "airframe"                % "0.64",
    "com.dripower" %% "play-circe" % "2609.2",
    // "org.webjars" % "swagger-ui" % "2.2.0"
  )
  // Adds additional packages into Twirl
  //TwirlKeys.templateImports += "com.github.j5ik2o.spetstore.controllers._"
  // Adds additional packages into conf/routes
  // play.sbt.routes.RoutesKeys.routesImport += "com.github.j5ik2o.spetstore.binders._"
).dependsOn(`db-interface`, domain).enablePlugins(PlayScala)

val dbDriver     = "com.mysql.jdbc.Driver"
val dbName       = "spetstore"
val dbUser       = "spetstore"
val dbPassword   = "passwd"
val dbPort: Int  = Utils.RandomPortSupport.temporaryServerPort()
val dbUrl        = s"jdbc:mysql://localhost:$dbPort/$dbName?useSSL=false"

lazy val flyway = (project in file("flyway"))
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "mysql" % "mysql-connector-java" % "5.1.42"
    ),
    parallelExecution in Test := false,
    wixMySQLVersion := com.wix.mysql.distribution.Version.v5_6_21,
    wixMySQLUserName := Some(dbUser),
    wixMySQLPassword := Some(dbPassword),
    wixMySQLSchemaName := dbName,
    wixMySQLPort := Some(dbPort),
    wixMySQLDownloadPath := Some(sys.env("HOME") + "/.wixMySQL/downloads"),
    wixMySQLTimeout := Some(2 minutes),
    flywayDriver := dbDriver,
    flywayUrl := dbUrl,
    flywayUser := dbUser,
    flywayPassword := dbPassword,
    flywaySchemas := Seq(dbName),
    flywayLocations := Seq(
      s"filesystem:${baseDirectory.value}/src/test/resources/rdb-migration/"
    ),
    flywayMigrate := (flywayMigrate dependsOn wixMySQLStart).value
  )
  .enablePlugins(FlywayPlugin)

lazy val `db-interface` = (project in file("db-interface"))
  .settings(commonSettings)
  .settings(
      name := "spetstore-db-interface",
      // JDBCのドライバークラス名を指定します(必須)
      driverClassName in generator := dbDriver,
      // JDBCの接続URLを指定します(必須)
      jdbcUrl in generator := dbUrl,
      // JDBCの接続ユーザ名を指定します(必須)
      jdbcUser in generator := dbUser,
      // JDBCの接続ユーザのパスワードを指定します(必須)
      jdbcPassword in generator := dbPassword,
      // カラム型名をどのクラスにマッピングするかを決める関数を記述します(必須)
      propertyTypeNameMapper in generator := {
        case "INTEGER" | "INT" | "TINYINT"     => "Int"
        case "BIGINT"                          => "Long"
        case "VARCHAR"                         => "String"
        case "BOOLEAN" | "BIT"                 => "Boolean"
        case "DATE" | "TIMESTAMP" | "DATETIME" => "java.time.ZonedDateTime"
        case "DECIMAL"                         => "BigDecimal"
        case "ENUM"                            => "String"
      },
      tableNameFilter in generator := { tableName: String =>
        (tableName.toUpperCase != "SCHEMA_VERSION") && (tableName
          .toUpperCase() != "FLYWAY_SCHEMA_HISTORY") && !tableName.toUpperCase
          .endsWith("ID_SEQUENCE_NUMBER")
      },
      outputDirectoryMapper in generator := {
        case s if s.endsWith("Spec") => (sourceDirectory in Test).value
        case s =>
          new java.io.File((scalaSource in Compile).value, "/spetstore/interface/dao")
      },
      // モデル名に対してどのテンプレートを利用するか指定できます。
      templateNameMapper in generator := {
        case className if className.endsWith("Spec") => "template_spec.ftl"
        case _                                       => "template.ftl"
      },
      generateAll in generator := Def
        .taskDyn {
          val ga = (generateAll in generator).value
          Def
            .task {
              (wixMySQLStop in flyway).value
            }
            .map(_ => ga)
        }
        .dependsOn(flywayMigrate in flyway)
        .value,
      //compile in Compile := ((compile in Compile) dependsOn (generateAll in generator)).value,
      libraryDependencies ++= Seq(
        "com.github.j5ik2o" %% "scala-ddd-base-slick" % "1.0.12",
        "javax.inject" % "javax.inject" % "1",
        "com.typesafe.play" %% "play-slick" % "3.0.1"
      ),
      parallelExecution in Test := false
  )
  .dependsOn(domain, flyway)
  .disablePlugins(WixMySQLPlugin)

lazy val localMySQL = (project in file("local-mysql"))
  .settings(commonSettings)
  .settings(
    name := "spetstore-local-mysql",
    libraryDependencies ++= Seq(
      "mysql" % "mysql-connector-java" % "5.1.42"
    ),
    wixMySQLVersion := com.wix.mysql.distribution.Version.v5_6_21,
    wixMySQLUserName := Some(dbUser),
    wixMySQLPassword := Some(dbPassword),
    wixMySQLSchemaName := dbName,
    wixMySQLPort := Some(3306),
    wixMySQLDownloadPath := Some(sys.env("HOME") + "/.wixMySQL/downloads"),
    wixMySQLTimeout := Some((30 seconds) * sys.env.getOrElse("SBT_TEST_TIME_FACTOR", "1").toDouble),
    flywayDriver := dbDriver,
    flywayUrl := s"jdbc:mysql://localhost:3306/$dbName?useSSL=false",
    flywayUser := dbUser,
    flywayPassword := dbPassword,
    flywaySchemas := Seq(dbName),
    flywayLocations := Seq(
      s"filesystem:${(baseDirectory in flyway).value}/src/test/resources/rdb-migration/",
      s"filesystem:${(baseDirectory in flyway).value}/src/test/resources/rdb-migration/test",
      s"filesystem:${baseDirectory.value}/src/main/resources/dummy-migration",
      s"filesystem:${(baseDirectory in flyway).value}/src/test/resources/open-beta-scope-migration",
      s"filesystem:${(baseDirectory in flyway).value}/src/test/resources/open-beta-table-migration",
      s"filesystem:${(baseDirectory in flyway).value}/src/test/resources/AST-434",
      s"filesystem:${(baseDirectory in flyway).value}/src/test/resources/API-81",
      s"filesystem:${(baseDirectory in flyway).value}/src/test/resources/API-80",
      s"filesystem:${(baseDirectory in flyway).value}/src/test/resources/API-318",
      s"filesystem:${(baseDirectory in flyway).value}/src/test/resources/API-344",
      s"filesystem:${(baseDirectory in flyway).value}/src/test/resources/API-341"
    ),
    flywayPlaceholderReplacement := true,
    flywayPlaceholders := Map(
      "engineName" -> "InnoDB",
      "idSequenceNumberEngineName" -> "MyISAM"
    ),
    run := (flywayMigrate dependsOn wixMySQLStart).value
  )
  .enablePlugins(FlywayPlugin)

lazy val root = (project in file(".")).settings(commonSettings).aggregate(api)



