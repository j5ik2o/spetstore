package spetstore.interface.api.controller

import java.time.ZonedDateTime

import akka.http.scaladsl.server.{ Directives, Route }
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.{ Content, Schema }
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import javax.ws.rs._
import monix.eval.Task
import monix.execution.Scheduler
import org.sisioh.baseunits.scala.money.Money
import spetstore.domain.model.basic.StatusType
import spetstore.domain.model.item._
import spetstore.interface.api.model.{ CreateItemRequest, CreateItemResponse, CreateItemResponseBody }
import spetstore.interface.generator.jdbc.ItemIdGeneratorOnJDBC
import spetstore.interface.repository.ItemRepository
import wvlet.airframe._

import scala.concurrent.Future

@Path("/items")
@Consumes(Array("application/json"))
@Produces(Array("application/json"))
trait ItemController extends Directives {

  private val itemRepository: ItemRepository[Task]         = bind[ItemRepository[Task]]
  private val itemIdGeneratorOnJDBC: ItemIdGeneratorOnJDBC = bind[ItemIdGeneratorOnJDBC]

  def route: Route = create

  private def convertToAggregate(id: ItemId, request: CreateItemRequest): Item = Item(
    id = id,
    status = StatusType.Enabled,
    name = ItemName(request.name),
    description = request.description.map(ItemDescription),
    categories = Categories(request.categories),
    price = Price(Money.yens(request.price)),
    createdAt = ZonedDateTime.now(),
    updatedAt = None
  )

  @POST
  @Operation(
    summary = "Create item",
    description = "Create Item",
    requestBody =
      new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[CreateItemResponse])))),
    responses = Array(
      new ApiResponse(responseCode = "200",
                      description = "Create response",
                      content = Array(new Content(schema = new Schema(implementation = classOf[CreateItemResponse])))),
      new ApiResponse(responseCode = "500", description = "Internal server error")
    )
  )
  def create: Route = path("items") {
    post {
      extractActorSystem { implicit system =>
        implicit val scheduler: Scheduler = Scheduler(system.dispatcher)
        entity(as[CreateItemRequest]) { request =>
          val future: Future[CreateItemResponse] = (for {
            itemId <- itemIdGeneratorOnJDBC.generateId()
            _      <- itemRepository.store(convertToAggregate(itemId, request))
          } yield CreateItemResponse(Right(CreateItemResponseBody(itemId.value.toString)))).runAsync
          onSuccess(future) { result =>
            complete(result)
          }
        }
      }
    }
  }

}
