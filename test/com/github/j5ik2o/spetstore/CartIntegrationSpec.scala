package com.github.j5ik2o.spetstore

import play.api.test.{WithServer, PlaySpecification}
import play.api.libs.ws.WS
import play.api.libs.json.Json

class CartIntegrationSpec extends PlaySpecification {

  private def createCart(port: Int): Long = {
    val body = """
    {
      "customerId": "453409080963371008",
      "cartItems": [
        {
          "no": 1,
          "itemId": "453409080963371008",
          "quantity": 1,
          "inStock": false
        }
      ]
    }
               """
    val f = WS.url(s"http://localhost:${port}/carts").post(Json.parse(body))
    val r = await(f)
    println(r.body)
    r.status must_== OK
    (Json.parse(r.body) \ "id").as[String].toLong
  }

  "create" in new WithServer {
    createCart(port) must not beNull
  }

  "get" in new WithServer {
    val id = createCart(port)
    println(id)
    id must not beNull
    val f = WS.url(s"http://localhost:${port}/cart/${id}").get
    val r = await(f)
    r.status must_== OK
  }

  "list" in new WithServer {
    val f = WS.url(s"http://localhost:${port}/carts").get
    val r = await(f)
    r.status must_== OK
  }

  "delete" in new WithServer {
    val id = createCart(port)
    println(id)
    id must not beNull
    val f = WS.url(s"http://localhost:${port}/cart/${id}").delete()
    val r = await(f)
    println(r.body)
    r.status must_== OK
  }


}
