package com.github.j5ik2o.spetstore

import play.api.Play.current
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.test.{PlaySpecification, WithServer}

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
    r.status must_== OK
    (Json.parse(r.body) \ "id").as[String].toLong
  }

  private def updateCart(port: Int, id: Long): Long = {
    val body = s"""
    {
      "customerId": "453409080963371008",
      "cartItems": [
        {
          "no": 1,
          "itemId": "453409080963371008",
          "quantity": 1,
          "inStock": false
        },
        {
          "no": 2,
          "itemId": "453409080963371008",
          "quantity": 1,
          "inStock": false
        }
      ]
    }
    """
    val f = WS.url(s"http://localhost:${port}/carts/${id}").put(Json.parse(body))
    val r = await(f)
    r.status must_== OK
    (Json.parse(r.body) \ "id").as[String].toLong
  }

  "CatController" should {

    "create the model" in new WithServer {
      createCart(port) must not beNull
    }

    "update the model by id" in new WithServer {
      val id = createCart(port)
      updateCart(port, id) must not beNull
    }

    "get the model by id" in new WithServer {
      val id = createCart(port)
      id must not beNull
      val f = WS.url(s"http://localhost:${port}/carts/${id}").get
      val r = await(f)
      r.status must_== OK
    }

    "get the models" in new WithServer {
      val f = WS.url(s"http://localhost:${port}/carts").get
      val r = await(f)
      r.status must_== OK
    }

    "delete the model by id" in new WithServer {
      val id = createCart(port)
      id must not beNull
      val f = WS.url(s"http://localhost:${port}/carts/${id}").delete()
      val r = await(f)
      r.status must_== OK
    }

  }

}
