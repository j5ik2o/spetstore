package spetstore.interface.api

import akka.http.scaladsl.model.{ ContentTypes, HttpEntity, HttpResponse }
import akka.http.scaladsl.server.{ Directives, Route, StandardRoute }
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import spetstore.interface.api.controller.{ CartController, ItemController, RouteLogging, UserAccountController }
import wvlet.airframe._

trait Routes extends Directives {

  private lazy val userAccountController = bind[UserAccountController]

  private lazy val itemController = bind[ItemController]

  private lazy val cartController = bind[CartController]

  private lazy val swaggerDocService = bind[SwaggerDocService]

  private def index(): StandardRoute = complete(
    HttpResponse(
      entity = HttpEntity(
        ContentTypes.`text/plain(UTF-8)`,
        "Wellcome to API"
      )
    )
  )

  def routes: Route = cors() {
    pathEndOrSingleSlash {
      index()
    } ~ path("swagger") {
      getFromResource("swagger/index.html")
    } ~ getFromResourceDirectory("swagger") ~
    extractExecutionContext { implicit ec =>
      extractMaterializer { implicit mat =>
        RouteLogging.default().httpLogRequestResult {
          swaggerDocService.routes ~ userAccountController.route ~ itemController.route ~ cartController.route
        }
      }
    }

  }

}
