package spetstore.interface.util

import com.typesafe.config.ConfigFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ BeforeAndAfter, BeforeAndAfterAll, Suite }
import slick.basic.DatabaseConfig
import slick.jdbc.SetParameter.SetUnit
import slick.jdbc.{ JdbcProfile, SQLActionBuilder }

import scala.concurrent.Future

trait Slick3SpecSupport extends BeforeAndAfter with BeforeAndAfterAll with ScalaFutures with JdbcSpecSupport {
  self: Suite with FlywayWithMySQLSpecSupport =>

  private var _dbConfig: DatabaseConfig[JdbcProfile] = _

  private var _profile: JdbcProfile = _

  protected def dbConfig = _dbConfig

  protected def profile = _profile

  after {
    implicit val ec = dbConfig.db.executor.executionContext
    val futures = tables.map { table =>
      val q = SQLActionBuilder(List(s"TRUNCATE TABLE $table"), SetUnit).asUpdate
      dbConfig.db.run(q)
    }
    Future.sequence(futures).futureValue
  }

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    val config = ConfigFactory.parseString(s"""
                                              |spetstore {
                                              |  interface {
                                              |    storage.jdbc {
                                              |      profile = "slick.jdbc.MySQLProfile$$"
                                              |      db {
                                              |        driver = "com.mysql.jdbc.Driver"
                                              |        url = "jdbc:mysql://localhost:$jdbcPort/spetstore?useSSL=false"
                                              |        user = "spetstore"
                                              |        password = "passwd"
                                              |        connectionPool = disabled
                                              |        keepAliveConnection = true
                                              |        properties = {
                                              |          maximumPoolSize = 4
                                              |          minimumIdle = 4
                                              |          connectionTimeout = 30
                                              |          idleTimeout = 30
                                              |        }
                                              |        poolName = "slick-pool"
                                              |        numThreads = 4
                                              |        queueSize = 1000
                                              |      }
                                              |    }
                                              |  }
                                              |}
      """.stripMargin)
    _dbConfig = DatabaseConfig.forConfig[JdbcProfile]("spetstore.interface.storage.jdbc", config)
    _profile = dbConfig.profile
  }

  override protected def afterAll(): Unit = {
    dbConfig.db.shutdown
    super.afterAll()
  }

}
