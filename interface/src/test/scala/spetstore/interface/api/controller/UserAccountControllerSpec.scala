package spetstore.interface.api.controller

import akka.http.scaladsl.model.StatusCodes
import io.circe.generic.auto._
import spetstore.interface.api.model.{
  CreateUserAccountRequestJson,
  CreateUserAccountResponseJson,
  ResolveUserAccountResponseJson
}

class UserAccountControllerSpec extends AbstractControllerSpec {

  private var controller: UserAccountController = _

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    controller = session.build[UserAccountController]
  }

  "UserAccountController" - {
    "create" in {
      val request = CreateUserAccountRequestJson("j5ik2o@gmail.com", "test", "Junichi", "KATO")
      Post("/user_accounts").withEntity(request.toHttpEntity) ~> controller.create ~> check {
        status shouldEqual StatusCodes.OK
        val response = responseAs[CreateUserAccountResponseJson]
        response.body.right.get.id.nonEmpty shouldBe true
      }
    }
    "resolveById" in {
      val request = CreateUserAccountRequestJson("j5ik2o@gmail.com", "test", "Junichi", "KATO")
      Post("/user_accounts").withEntity(request.toHttpEntity) ~> controller.create ~> check {
        status shouldEqual StatusCodes.OK
        val response = responseAs[CreateUserAccountResponseJson]
        val id       = response.body.right.get.id
        Get(s"/user_accounts/$id") ~> controller.resolveById ~> check {
          status shouldEqual StatusCodes.OK
          val response = responseAs[ResolveUserAccountResponseJson]
          response.body.right.get.id.nonEmpty shouldBe true
          response.body.right.get.emailAddress shouldBe request.emailAddress
          response.body.right.get.firstName shouldBe request.firstName
          response.body.right.get.lastName shouldBe request.lastName
          response.body.right.get.createdAt should be >= (0L)
          response.body.right.get.updatedAt should be >= (0L)
        }
      }
    }
  }

}
