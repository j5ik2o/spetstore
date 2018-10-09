package spetstore.interface.api.controller

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.scaladsl.{ Sink, Source }
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.{ Content, Schema }
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.{ Operation, Parameter }
import javax.ws.rs._
import spetstore.domain.model.UserAccountId
import spetstore.interface.api.model.{ ResolveUserAccountResponseJson, _ }
import spetstore.useCase.UserAccountUseCase
import spetstore.useCase.model.CreateUserAccountRequest
import wvlet.airframe._

import scala.concurrent.Future

@Path("/user_accounts")
@Consumes(Array("application/json"))
@Produces(Array("application/json"))
trait UserAccountController extends BaseController {

  private val userAccountUseCase = bind[UserAccountUseCase]

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
        content = Array(new Content(schema = new Schema(implementation = classOf[ResolveUserAccountResponseJson])))
      ),
      new ApiResponse(responseCode = "400", description = "Bad request"),
      new ApiResponse(responseCode = "500", description = "Internal server error")
    )
  )
  def resolveById: Route = path("user_accounts" / Segment) { id: String =>
    get {
      extractMaterializer { implicit mat =>
        extractScheduler { implicit scheduler =>
          extractAggregateId(UserAccountId)(id) { userAccountId =>
            val future = userAccountUseCase
              .resolveById(userAccountId).map { response =>
                ResolveUserAccountResponseJson(
                  Right(
                    ResolveUserAccountResponseBody(
                      id = hashids.encode(response.id.value),
                      emailAddress = response.emailAddress.value,
                      firstName = response.firstName,
                      lastName = response.lastName,
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
    summary = "Create UserAccount",
    description = "Create UserAccount",
    requestBody = new RequestBody(
      content = Array(new Content(schema = new Schema(implementation = classOf[CreateUserAccountRequestJson])))
    ),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "Create response",
        content = Array(new Content(schema = new Schema(implementation = classOf[CreateUserAccountResponseJson])))
      ),
      new ApiResponse(responseCode = "400", description = "Bad request"),
      new ApiResponse(responseCode = "500", description = "Internal server error")
    )
  )
  def create: Route = path("user_accounts") {
    post {
      extractMaterializer { implicit mat =>
        extractScheduler { implicit scheduler =>
          entity(as[CreateUserAccountRequestJson]) { request =>
            val future: Future[CreateUserAccountResponseJson] = Source
              .single(request).map { request =>
                CreateUserAccountRequest(request.emailAddress, request.password, request.firstName, request.lastName)
              }.via(userAccountUseCase.create).map { response =>
                CreateUserAccountResponseJson(Right(CreateUserAccountResponseBody(hashids.encode(response.id))))
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
