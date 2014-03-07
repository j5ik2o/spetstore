package com.github.j5ik2o.spetstore.application.json

import play.api.libs.functional.syntax._
import play.api.libs.json._
import com.github.j5ik2o.spetstore.domain.model.customer.Customer

trait CustomerJsonSupport {

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

  implicit object CustomerJsonConverter extends Reads[CustomerJson] with Writes[Customer] {

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

    override def writes(o: Customer): JsValue = {
      JsObject(
        Seq(
          "name" -> JsString(o.name),
          "zipCode" -> JsString(o.profile.postalAddress.zipCode.asString),
          "prefCode" -> JsNumber(o.profile.postalAddress.pref.id),
          "cityName" -> JsString(o.profile.postalAddress.cityName),
          "addressName" -> JsString(o.profile.postalAddress.addressName),
          "buildingName" -> o.profile.postalAddress.buildingName.map(JsString).getOrElse(JsNull),
          "email" -> JsString(o.profile.contact.email),
          "phone" -> JsString(o.profile.contact.phone),
          "loginName" -> JsString(o.config.loginName),
          "password" -> JsString(o.config.password)
        )
      )
    }

  }


}
