package spetstore.interface.util

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{ Seconds, Span }

trait ScalaFuturesSpecSupport {
  this: ScalaFutures =>
  override implicit def patienceConfig: PatienceConfig =
    PatienceConfig(timeout = scaled(Span(60, Seconds)), interval = scaled(Span(1, Seconds)))
}
