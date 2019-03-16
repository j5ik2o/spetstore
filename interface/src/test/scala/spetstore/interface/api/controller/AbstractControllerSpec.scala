package spetstore.interface.api.controller

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ HttpEntity, MediaTypes }
import akka.http.scaladsl.testkit.{ RouteTestTimeout, ScalatestRouteTest }
import akka.testkit.TestKit
import akka.util.ByteString
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.Encoder
import io.circe.syntax._
import monix.execution.Scheduler
import org.scalatest.{ FreeSpec, Matchers }
import spetstore.interface
import spetstore.interface.util.{ FlywayWithMySQLSpecSupport, Slick3SpecSupport }
import wvlet.airframe.{ newDesign, Design, Session }

import scala.concurrent.duration._

class AbstractControllerSpec
    extends FreeSpec
    with FlywayWithMySQLSpecSupport
    with Slick3SpecSupport
    with ScalatestRouteTest
    with Matchers
    with FailFastCirceSupport {
  implicit def timeout: RouteTestTimeout = RouteTestTimeout(5 seconds)
  override val tables: Seq[String]       = Seq("user_account")

  private var _session: Session     = _
  private var _scheduler: Scheduler = _

  implicit class ToHttpEntityOps[A: Encoder](json: A) {
    def toHttpEntity: HttpEntity.Strict = {
      val jsonAsByteString = ByteString(json.asJson.noSpaces)
      HttpEntity(MediaTypes.`application/json`, jsonAsByteString)
    }
  }

  def session: Session = _session

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    _scheduler = Scheduler(system.dispatcher)
    val design: Design =
      newDesign
        .bind[ActorSystem].toInstance(system)
        .add(
          interface.createInterfaceDesign(
            "0.0.0.0",
            8080,
            "abc",
            Set(classOf[UserAccountController], classOf[ItemController], classOf[CartController]),
            dbConfig
          )(system)
        )
    _session = design.newSession
    _session.start

  }

  override protected def afterAll(): Unit = {
    _session.shutdown
    TestKit.shutdownActorSystem(system)
    super.afterAll()
  }

}
