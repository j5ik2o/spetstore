//package com.github.j5ik2o.spetstore
//
//import play.api.Play.current
//import play.api.libs.json.Json
//import play.api.libs.ws.WS
//import play.api.test._
//
//class CategoryIntegrationSpec extends PlaySpecification {
//
//  private def createCategory(port: Int): Long = {
//    val body = """
//    {
//      "name": "hoge",
//      "description": "fuga"
//    }
//    """
//    val f = WS.url(s"http://localhost:${port}/categories").post(Json.parse(body))
//    val r = await(f)
//    r.status must_== OK
//    (Json.parse(r.body) \ "id").as[String].toLong
//  }
//
//  private def updateCategory(port: Int, id: Long): Long = {
//    val body = s"""
//    {
//      "name": "hoge",
//      "description": "hoge"
//    }
//    """
//    val f = WS.url(s"http://localhost:${port}/categories/${id}").put(Json.parse(body))
//    val r = await(f)
//    r.status must_== OK
//    (Json.parse(r.body) \ "id").as[String].toLong
//  }
//
//  "CategoryController" should {
//
//    "create the model" in new WithServer {
//      createCategory(port) must not beNull
//    }
//
//    "update the model by id" in new WithServer {
//      val id = createCategory(port)
//      updateCategory(port, id) must not beNull
//    }
//
//    "get the model" in new WithServer {
//      val id = createCategory(port)
//      id must not beNull
//      val f = WS.url(s"http://localhost:${port}/categories/${id}").get
//      val r = await(f)
//      r.status must_== OK
//    }
//
//    "get models" in new WithServer {
//      val f = WS.url(s"http://localhost:${port}/categories").get
//      val r = await(f)
//      r.status must_== OK
//    }
//
//    "delete the model by id" in new WithServer {
//      val id = createCategory(port)
//      id must not beNull
//      val f = WS.url(s"http://localhost:${port}/categories/${id}").delete()
//      val r = await(f)
//      r.status must_== OK
//    }
//
//  }
//}
