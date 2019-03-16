package spetstore.interface.api.controller

import akka.http.scaladsl.model.StatusCodes
import io.circe.generic.auto._
import spetstore.interface.api.model.{ CreateItemRequestJson, CreateItemResponseJson, ResolveItemResponseJson }

class ItemControllerSpec extends AbstractControllerSpec {

  override val tables: Seq[String] = Seq("item")

  private var controller: ItemController = _

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    controller = session.build[ItemController]
  }

  "ItemController" - {
    "create" in {
      val request = CreateItemRequestJson("cat", None, Set.empty, 100)
      Post("/items").withEntity(request.toHttpEntity) ~> controller.create ~> check {
        status shouldBe StatusCodes.OK
        val response = responseAs[CreateItemResponseJson]
        response.body.right.get.id.nonEmpty shouldBe true
      }
    }
    "resolveById" in {
      val request = CreateItemRequestJson("cat", None, Set.empty, 100)
      Post("/items").withEntity(request.toHttpEntity) ~> controller.create ~> check {
        status shouldBe StatusCodes.OK
        val response = responseAs[CreateItemResponseJson]
        val id       = response.body.right.get.id
        Get(s"/items/$id") ~> controller.resolveById ~> check {
          status shouldBe StatusCodes.OK
          val response = responseAs[ResolveItemResponseJson]
          response.body.right.get.name shouldBe request.name
          response.body.right.get.categories shouldBe request.categories
          response.body.right.get.description shouldBe request.description
          response.body.right.get.price shouldBe request.price
          response.body.right.get.createdAt should be >= (0L)
        }
      }
    }
  }
}
