package spetstore.interface.api.controller

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ Directive1, ExceptionHandler, RejectionHandler, Route }
import com.github.j5ik2o.dddbase.AggregateNotFoundException
import com.github.j5ik2o.reactive.redis.RedisConnectionPool
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import monix.eval.Task
import monix.execution.Scheduler
import org.hashids.Hashids
import spetstore.interface.api.model.{ ErrorResponseBody, ResolveUserAccountResponseJson }
import spetstore.interface.api.rejection.MalformedPathRejection
import wvlet.airframe.bind

trait BaseController {

  def route: Route

  protected val hashids = bind[Hashids]

  protected val connectionPool = bind[RedisConnectionPool[Task]]

  protected def exceptionHandler: ExceptionHandler =
    ExceptionHandler({
      case ex: AggregateNotFoundException =>
        complete(
          (NotFound, ResolveUserAccountResponseJson(Left(ErrorResponseBody(ex.getMessage, "0404"))))
        )
    })

  protected def rejectionHandler: RejectionHandler =
    RejectionHandler
      .newBuilder().handle {
        case r: MalformedPathRejection =>
          complete((BadRequest, ResolveUserAccountResponseJson(Left(ErrorResponseBody(r.errorMsg, "0400")))))
      }.result()

  protected def extractScheduler: Directive1[Scheduler] = extractActorSystem.tmap { s =>
    Scheduler(s._1.dispatcher)
  }

  protected def extractAggregateId[ID](idValueF: Long => ID)(id: String): Directive1[ID] =
    try {
      val result = hashids.decode(id)
      if (result.isEmpty)
        reject(MalformedPathRejection("id", "The Path is malformed"))
      else
        provide(idValueF(result(0)))
    } catch {
      case ex: IllegalArgumentException =>
        reject(MalformedPathRejection("id", "The Path is malformed", Some(ex)))
    }

}
