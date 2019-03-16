package spetstore.interface.util

import org.scalatest.concurrent.ScalaFutures

trait JdbcSpecSupport extends ScalaFutures with ScalaFuturesSpecSupport {
  this: FlywayWithMySQLSpecSupport =>
  val tables: Seq[String]

  def jdbcPort: Int = mySQLdConfig.port.get

}
