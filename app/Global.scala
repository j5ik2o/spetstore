import com.fasterxml.jackson.core.JsonParseException
import com.github.j5ik2o.spetstore.application.GuiceModule
import com.google.inject.Guice
import play.api._
import play.api.mvc.{Results, SimpleResult, RequestHeader}
import scala.concurrent._

object Global extends GlobalSettings {

  private lazy val injector = Guice.createInjector(new GuiceModule)

  override def getControllerInstance[A](clazz: Class[A]) = {
    injector.getInstance(clazz)
  }

  override def onError(request: RequestHeader, ex: Throwable): Future[SimpleResult] =  {
    ex match {
      case ex: JsonParseException => Future.successful(Results.BadRequest)
      case _ => Future.failed(ex)
    }
  }

}
