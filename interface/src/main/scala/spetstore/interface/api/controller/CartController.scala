package spetstore.interface.api.controller

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.{ Sink, Source }
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.{ Content, Schema }
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.{ Operation, Parameter }
import javax.ws.rs._
import monix.eval.Task
import spetstore.domain.model.UserAccountId
import spetstore.domain.model.purchase.CartId
import spetstore.interface.api.model._
import spetstore.useCase.CartUseCase
import spetstore.useCase.model.CreateCartRequest
import wvlet.airframe.bind

@Path("/carts")
@Consumes(Array("application/json"))
@Produces(Array("application/json"))
trait CartController extends BaseController {

  private val cartUseCase = bind[CartUseCase]

  override def route: Route = handleRejections(rejectionHandler) {
    handleExceptions(exceptionHandler) {
      create ~ resolveById
    }
  }

  @GET
  @Path("{id}")
  @Operation(
    summary = "Get Cart",
    description = "Get Cart",
    parameters = Array(
      new Parameter(in = ParameterIn.PATH,
                    name = "id",
                    required = true,
                    description = "cart id",
                    allowEmptyValue = false,
                    allowReserved = true)
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "Get response",
        content = Array(new Content(schema = new Schema(implementation = classOf[ResolveCartResponseJson])))
      ),
      new ApiResponse(responseCode = "400", description = "Bad request"),
      new ApiResponse(responseCode = "500", description = "Internal server error")
    )
  )
  def resolveById: Route = path("carts" / Segment) { id: String =>
    get {
      extractMaterializer { implicit mat =>
        extractScheduler { implicit scheduler =>
          provide(CartId(id)) { cartId =>
            val task = connectionPool.withConnectionF { implicit conn =>
              Task.deferFuture {
                cartUseCase
                  .resolveById(cartId).map { response =>
                    ResolveCartResponseJson(
                      Right(
                        ResolveCartResponseBody(
                          id = response.id.value,
                          userAccountId = hashids.encode(response.userAccountId.value),
                          cartItems = null,
                          createdAt = response.createdAt.millisecondsFromEpoc,
                          updatedAt = response.updatedAt.map(_.millisecondsFromEpoc)
                        )
                      )
                    )
                  }.runWith(Sink.head)
              }
            }
            onSuccess(task.runAsync) { result =>
              complete(result)
            }
          }
        }
      }
    }
  }

  @POST
  @Operation(
    summary = "Create Cart",
    description = "Create Cart",
    requestBody = new RequestBody(
      content = Array(new Content(schema = new Schema(implementation = classOf[CreateCartRequestJson])))
    ),
    responses = Array(
      new ApiResponse(responseCode = "200",
                      description = "Create response",
                      content =
                        Array(new Content(schema = new Schema(implementation = classOf[CreateCartResponseJson])))),
      new ApiResponse(responseCode = "400", description = "Bad request"),
      new ApiResponse(responseCode = "500", description = "Internal server error")
    )
  )
  def create: Route = path("carts") {
    post {
      extractLog { log =>
        extractMaterializer { implicit mat =>
          extractScheduler { implicit scheduler =>
            entity(as[CreateCartRequestJson]) { request =>
              extractAggregateId(UserAccountId)(request.userAccountId) { uaid =>
                val task = connectionPool.withConnectionF { implicit conn =>
                  Task.deferFuture {
                    Source
                      .single(uaid).map(CreateCartRequest).via(cartUseCase.create).map { response =>
                        CreateItemResponseJson(Right(CreateItemResponseBody(response.cartId)))
                      }.runWith(Sink.head)
                  }
                }
                onSuccess(task.runAsync) { result =>
                  complete(result)
                }
              }
            }
          }
        }
      }
    }
  }

}
