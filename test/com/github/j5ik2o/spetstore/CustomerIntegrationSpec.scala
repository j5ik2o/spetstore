package com.github.j5ik2o.spetstore

import play.api.libs.ws.WS
import play.api.test._
import play.api.libs.json.Json

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

  "create" in new WithServer {
    createCustomer(port) must not beNull
  }

  "get" in new WithServer {
    val id = createCustomer(port)
    println(id)
    id must not beNull
    val f = WS.url(s"http://localhost:${port}/customer/${id}").get
    val r = await(f)
    r.status must_== OK
  }

  "list" in new WithServer {
    val f = WS.url(s"http://localhost:${port}/customers").get
    val r = await(f)
    r.status must_== OK
  }

  "delete" in new WithServer {
    val id = createCustomer(port)
    println(id)
    id must not beNull
    val f = WS.url(s"http://localhost:${port}/customer/${id}").delete()
    val r = await(f)
    println(r.body)
    r.status must_== OK
  }




}
