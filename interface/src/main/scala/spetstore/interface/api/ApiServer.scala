package spetstore.interface.api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.settings.ServerSettings
import akka.stream.ActorMaterializer
import wvlet.airframe._

import scala.concurrent.Future
import scala.util.{ Failure, Success }

trait ApiServer {

  implicit val system           = bind[ActorSystem]
  implicit val materializer     = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  private val routes = bind[Routes].routes

  def start(host: String, port: Int, settings: ServerSettings): Future[ServerBinding] = {
    val bindingFuture = Http().bindAndHandle(handler = routes, interface = host, port = port, settings = settings)
    bindingFuture.onComplete {
      case Success(binding) =>
        system.log.info(s"Server online at http://${binding.localAddress.getHostName}:${binding.localAddress.getPort}/")
      case Failure(ex) =>
        system.log.error(ex, "occurred error")
    }
    sys.addShutdownHook {
      bindingFuture
        .flatMap(_.unbind())
        .onComplete { _ =>
          materializer.shutdown()
          system.terminate()
        }
    }
    bindingFuture
  }

}
