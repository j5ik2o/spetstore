import javax.inject.{ Inject, Singleton }

import com.fasterxml.jackson.core.JsonParseException
import play.api.http.{ DefaultHttpErrorHandler, HttpErrorHandler }
import play.api.mvc.{ RequestHeader, Result, Results }

import scala.concurrent.Future

@Singleton
class ErrorHandler @Inject() (defaultHttpErrorHandler: DefaultHttpErrorHandler)
    extends HttpErrorHandler {

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    exception match {
      case _: JsonParseException => Future.successful(Results.BadRequest)
      case _ => Future.failed(exception)
    }
  }

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] =
    defaultHttpErrorHandler.onClientError(request, statusCode, message)
}