package spetstore.interface.api.controller

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import javax.ws.rs.{ Consumes, Path, Produces }

@Path("/carts")
@Consumes(Array("application/json"))
@Produces(Array("application/json"))
trait CartController extends BaseController {

  def create: Route = path("carts") {
    complete("")
  }

}
