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
import org.hashids.Hashids
import org.sisioh.baseunits.scala.money.Money
import org.sisioh.baseunits.scala.timeutil.Clock
import spetstore.domain.model.basic.{ Price, StatusType }
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

  private val itemRepository: ItemRepository[Task] = bind[ItemRepository[Task]]

  private val itemIdGeneratorOnJDBC: ItemIdGeneratorOnJDBC = bind[ItemIdGeneratorOnJDBC]

  private val hashids = bind[Hashids]

  def route: Route = create

  private def convertToAggregate(id: ItemId, request: CreateItemRequest): Item = Item(
    id = id,
    status = StatusType.Active,
    name = ItemName(request.name),
    description = request.description.map(ItemDescription),
    categories = Categories(request.categories),
    price = Price(Money.yens(request.price)),
    createdAt = Clock.now,
    updatedAt = None
  )

  @POST
  @Operation(
    summary = "Create item",
    description = "Create item",
    requestBody =
      new RequestBody(content = Array(new Content(schema = new Schema(implementation = classOf[CreateItemRequest])))),
    responses = Array(
      new ApiResponse(responseCode = "200",
                      description = "Create response",
                      content = Array(new Content(schema = new Schema(implementation = classOf[CreateItemResponse])))),
      new ApiResponse(responseCode = "400", description = "Bad request"),
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
          } yield CreateItemResponse(Right(CreateItemResponseBody(hashids.encode(itemId.value))))).runToFuture
          onSuccess(future) { result =>
            complete(result)
          }
        }
      }
    }
  }

}
