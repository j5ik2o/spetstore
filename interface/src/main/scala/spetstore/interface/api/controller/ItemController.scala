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
import monix.execution.Scheduler
import spetstore.domain.model.item.ItemId
import spetstore.interface.api.model.{ CreateItemResponseBody, _ }
import spetstore.useCase.ItemUseCase
import spetstore.useCase.model.CreateItemRequest
import wvlet.airframe._

import scala.concurrent.Future

@Path("/items")
@Consumes(Array("application/json"))
@Produces(Array("application/json"))
trait ItemController extends BaseController {

  private val itemUseCase = bind[ItemUseCase]

  override def route: Route = handleRejections(rejectionHandler) {
    handleExceptions(exceptionHandler) {
      create ~ resolveById
    }
  }

  @GET
  @Path("{id}")
  @Operation(
    summary = "Get UserAccount",
    description = "Get UserAccount",
    parameters = Array(
      new Parameter(in = ParameterIn.PATH,
                    name = "id",
                    required = true,
                    description = "user account id",
                    allowEmptyValue = false,
                    allowReserved = true)
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "Get response",
        content = Array(new Content(schema = new Schema(implementation = classOf[ResolveItemResponseJson])))
      ),
      new ApiResponse(responseCode = "400", description = "Bad request"),
      new ApiResponse(responseCode = "500", description = "Internal server error")
    )
  )
  def resolveById: Route = path("items" / Segment) { id: String =>
    get {
      extractMaterializer { implicit mat =>
        extractScheduler { implicit scheduler =>
          extractAggregateId(ItemId)(id) { itemId =>
            val future = itemUseCase
              .resolveById(itemId).map { response =>
                ResolveItemResponseJson(
                  Right(
                    ResolveItemResponseBody(
                      id = hashids.encode(response.id.value),
                      name = response.name.breachEncapsulationOfValue,
                      description = response.description.map(_.breachEncapsulationOfValue),
                      categories = response.categories.breachEncapsulationOfValues,
                      price = response.price.breachEncapsulationOfValue.amount.toLong,
                      createdAt = response.createdAt.millisecondsFromEpoc,
                      updatedAt = response.updatedAt.map(_.millisecondsFromEpoc)
                    )
                  )
                )
              }.runWith(Sink.head)
            onSuccess(future) { result =>
              complete(result)
            }
          }
        }
      }
    }
  }

  @POST
  @Operation(
    summary = "Create Item",
    description = "Create Item",
    requestBody = new RequestBody(
      content = Array(new Content(schema = new Schema(implementation = classOf[CreateItemRequestJson])))
    ),
    responses = Array(
      new ApiResponse(responseCode = "200",
                      description = "Create response",
                      content =
                        Array(new Content(schema = new Schema(implementation = classOf[CreateItemResponseJson])))),
      new ApiResponse(responseCode = "400", description = "Bad request"),
      new ApiResponse(responseCode = "500", description = "Internal server error")
    )
  )
  def create: Route = path("items") {
    post {
      extractActorSystem { implicit system =>
        implicit val scheduler: Scheduler = Scheduler(system.dispatcher)
        extractMaterializer { implicit mat =>
          extractScheduler { implicit scheduler =>
            entity(as[CreateItemRequestJson]) { request =>
              val future: Future[CreateItemResponseJson] = Source
                .single(request).map { request =>
                  CreateItemRequest(request.name, request.description, request.categories, request.price)
                }.via(itemUseCase.create).map { response =>
                  CreateItemResponseJson(Right(CreateItemResponseBody(hashids.encode(response.id.value))))
                }.runWith(Sink.head)
              onSuccess(future) { result =>
                complete(result)
              }
            }
          }
        }
      }
    }
  }

}
