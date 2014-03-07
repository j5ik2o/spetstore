package com.github.j5ik2o.spetstore.application.json

import play.api.libs.functional.syntax._
import play.api.libs.json._

trait CustomerJsonSupport {

  implicit object CustomerJsonConverter extends Reads[CustomerJson] {

    def reads(json: JsValue): JsResult[CustomerJson] = {
      ((__ \ 'name).read[String] and
        (__ \ 'zipCode1).read[String] and
        (__ \ 'zipCode2).read[String] and
        (__ \ 'prefCode).read[Int] and
        (__ \ 'cityName).read[String] and
        (__ \ 'addressName).read[String] and
        (__ \ 'buildingName).readNullable[String] and
        (__ \ 'email).read[String] and
        (__ \ 'phone).read[String] and
        (__ \ 'loginName).read[String] and
        (__ \ 'password).read[String])(CustomerJson.apply _).reads(json)
    }

  }

  case class CustomerJson(name: String,
                          zipCode1: String,
                          zipCode2: String,
                          prefCode: Int,
                          cityName: String,
                          addressName: String,
                          buildingName: Option[String],
                          email: String,
                          phone: String,
                          loginName: String,
                          password: String)

}
