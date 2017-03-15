//package com.github.j5ik2o.spetstore
//
//import play.api.Play.current
//import play.api.libs.json.Json
//import play.api.libs.ws.WS
//import play.api.test._
//
//class ItemIntegrationSpec extends PlaySpecification {
//
//  private def createItem(port: Int): Long = {
//    val body = """
//    {
//      "itemTypeId": "453409080963371008",
//      "name": "hoge",
//      "description": "fuga",
//      "price": "123",
//      "supplierId": "453409080963371008"
//    }
//    """
//    val f = WS.url(s"http://localhost:${port}/items").post(Json.parse(body))
//    val r = await(f)
//    r.status must_== OK
//    (Json.parse(r.body) \ "id").as[String].toLong
//  }
//
//  private def updateItem(port: Int, id: Long): Long = {
//    val body = s"""
//    {
//      "itemTypeId": "453409080963371008",
//      "name": "hoge",
//      "description": "fuga",
//      "price": "123",
//      "supplierId": "453409080963371008"
//    }
//    """
//    val f = WS.url(s"http://localhost:${port}/items/${id}").put(Json.parse(body))
//    val r = await(f)
//    r.status must_== OK
//    (Json.parse(r.body) \ "id").as[String].toLong
//  }
//
//  "ItemController" should {
//
//    "create the model" in new WithServer {
//      createItem(port) must not beNull
//    }
//
//    "update the model by id" in new WithServer {
//      val id = createItem(port)
//      updateItem(port, id) must not beNull
//    }
//
//    "get the model" in new WithServer {
//      val id = createItem(port)
//      println(id)
//      id must not beNull
//      val f = WS.url(s"http://localhost:${port}/items/${id}").get
//      val r = await(f)
//      r.status must_== OK
//    }
//
//    "get models" in new WithServer {
//      val f = WS.url(s"http://localhost:${port}/items").get
//      val r = await(f)
//      r.status must_== OK
//    }
//
//    "delete the model by id" in new WithServer {
//      val id = createItem(port)
//      println(id)
//      id must not beNull
//      val f = WS.url(s"http://localhost:${port}/items/${id}").delete()
//      val r = await(f)
//      r.status must_== OK
//    }
//
//  }
//}
