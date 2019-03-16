package spetstore.interface.api.controller

import akka.http.scaladsl.model.StatusCodes
import io.circe.generic.auto._
import spetstore.interface.api.model._

class CartControllerSpec extends AbstractControllerSpec {

  override val tables: Seq[String] = Seq.empty

  private var userAccountController: UserAccountController = _
  private var cartController: CartController               = _

  protected override def beforeAll(): Unit = {
    super.beforeAll()
    userAccountController = session.build[UserAccountController]
    cartController = session.build[CartController]
  }

  "CartController" - {
    "create" in {
      val request = CreateUserAccountRequestJson("j5ik2o@gmail.com", "test", "Junichi", "KATO")
      Post("/user_accounts").withEntity(request.toHttpEntity) ~> userAccountController.create ~> check {
        status shouldEqual StatusCodes.OK
        val createUserResponse = responseAs[CreateUserAccountResponseJson]
        println(createUserResponse)
        val request = CreateCartRequestJson(createUserResponse.body.right.get.id)
        Post("/carts").withEntity(request.toHttpEntity) ~> cartController.create ~> check {
          status shouldBe StatusCodes.OK
          val response = responseAs[CreateCartResponseJson]
          response.body.right.get.cartId.nonEmpty shouldBe true
        }
      }
    }
    "resolveById" in {
      val request = CreateUserAccountRequestJson("j5ik2o@gmail.com", "test", "Junichi", "KATO")
      Post("/user_accounts").withEntity(request.toHttpEntity) ~> userAccountController.create ~> check {
        status shouldEqual StatusCodes.OK
        val createUserResponse = responseAs[CreateUserAccountResponseJson]
        println(createUserResponse)
        val request = CreateCartRequestJson(createUserResponse.body.right.get.id)
        Post("/carts").withEntity(request.toHttpEntity) ~> cartController.create ~> check {
          status shouldBe StatusCodes.OK
          val response = responseAs[CreateCartResponseJson]
          val id       = response.body.right.get.cartId
          Get(s"/carts/$id") ~> cartController.resolveById ~> check {
            status shouldBe StatusCodes.OK
            val response = responseAs[ResolveCartResponseJson]
            response.body.right.get.id shouldBe id
          }
        }
      }
    }
  }
}
