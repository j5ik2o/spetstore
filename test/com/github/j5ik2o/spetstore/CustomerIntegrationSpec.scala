package com.github.j5ik2o.spetstore

import play.api.Play.current
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.test._

class CustomerIntegrationSpec extends PlaySpecification {

  private def createCustomer(port: Int): Long = {
    val body = """
    {
      "name": "hoge",
      "sexType": 1,
      "zipCode1": "1000",
      "zipCode2": "001",
      "prefCode": 1,
      "cityName": "hoge",
      "addressName": "fuga",
      "buildingName": "aaa",
      "email": "j5ik2o@gmail.com",
      "phone": "00-0000-0000",
      "loginName": "hoge",
      "password": "fuga"
    }
               """
    val f = WS.url(s"http://localhost:${port}/customers").post(Json.parse(body))
    val r = await(f)
    r.status must_== OK
    (Json.parse(r.body) \ "id").as[String].toLong
  }

  private def updateCustomer(port: Int, id: Long): Long = {
    val body = s"""
    {
      "name": "hoge",
      "sexType": 1,
      "zipCode1": "1000",
      "zipCode2": "001",
      "prefCode": 1,
      "cityName": "hoge",
      "addressName": "fuga",
      "buildingName": "aaa",
      "email": "j5ik2o@gmail.com",
      "phone": "00-0000-0000",
      "loginName": "hoge",
      "password": "fuga"
    }
    """
    val f = WS.url(s"http://localhost:${port}/customers/${id}").put(Json.parse(body))
    val r = await(f)
    r.status must_== OK
    (Json.parse(r.body) \ "id").as[String].toLong
  }

  "CustomerController" should {

    "create the model" in new WithServer {
      createCustomer(port) must not beNull
    }

    "update the model by id" in new WithServer {
      val id = createCustomer(port)
      updateCustomer(port, id) must not beNull
    }

    "get the model" in new WithServer {
      val id = createCustomer(port)
      id must not beNull
      val f = WS.url(s"http://localhost:${port}/customers/${id}").get
      val r = await(f)
      r.status must_== OK
    }

    "get models" in new WithServer {
      val f = WS.url(s"http://localhost:${port}/customers").get
      val r = await(f)
      r.status must_== OK
    }

    "delete the model by id" in new WithServer {
      val id = createCustomer(port)
      id must not beNull
      val f = WS.url(s"http://localhost:${port}/customers/${id}").delete()
      val r = await(f)
      r.status must_== OK
    }

  }

}
