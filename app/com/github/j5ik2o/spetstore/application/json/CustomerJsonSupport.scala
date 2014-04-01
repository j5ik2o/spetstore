package com.github.j5ik2o.spetstore.application.json

import com.github.j5ik2o.spetstore.application.controller.CustomerController
import com.github.j5ik2o.spetstore.domain.model.basic._
import com.github.j5ik2o.spetstore.domain.model.customer.Customer
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerConfig
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerId
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerProfile
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class CustomerJson(id: Option[String],
                        name: String,
                        sexType: Int,
                        zipCode1: String,
                        zipCode2: String,
                        prefCode: Int,
                        cityName: String,
                        addressName: String,
                        buildingName: Option[String],
                        email: String,
                        phone: String,
                        loginName: String,
                        password: String,
                        favoriteCategoryId: Option[String])

trait CustomerJsonSupport {
  this: CustomerController =>

  protected def convertToEntity(customerJson: CustomerJson): Customer =
    Customer(
      id = CustomerId(customerJson.id.map(_.toLong).get),
      status = StatusType.Enabled,
      name = customerJson.name,
      sexType = SexType(customerJson.sexType),
      profile = CustomerProfile(
        postalAddress = PostalAddress(
          ZipCode(customerJson.zipCode1, customerJson.zipCode2),
          Pref(customerJson.prefCode),
          customerJson.cityName,
          customerJson.addressName,
          customerJson.buildingName
        ),
        contact = Contact(customerJson.email, customerJson.phone)
      ),
      config = CustomerConfig(
        loginName = customerJson.loginName,
        password = customerJson.password,
        favoriteCategoryId = None
      )
    )

  protected def convertToEntityWithoutId(customerJson: CustomerJson): Customer =
    Customer(
      id = CustomerId(identifierService.generate),
      status = StatusType.Enabled,
      name = customerJson.name,
      sexType = SexType(customerJson.sexType),
      profile = CustomerProfile(
        postalAddress = PostalAddress(
          ZipCode(customerJson.zipCode1, customerJson.zipCode2),
          Pref(customerJson.prefCode),
          customerJson.cityName,
          customerJson.addressName,
          customerJson.buildingName
        ),
        contact = Contact(customerJson.email, customerJson.phone)
      ),
      config = CustomerConfig(
        loginName = customerJson.loginName,
        password = customerJson.password,
        favoriteCategoryId = None
      )
    )




  implicit object JsonConverter extends Reads[CustomerJson] with Writes[Customer] {

    def reads(json: JsValue): JsResult[CustomerJson] = {
      ((__ \ 'id).readNullable[String] and
        (__ \ 'name).read[String] and
        (__ \ 'sexType).read[Int] and
        (__ \ 'zipCode1).read[String] and
        (__ \ 'zipCode2).read[String] and
        (__ \ 'prefCode).read[Int] and
        (__ \ 'cityName).read[String] and
        (__ \ 'addressName).read[String] and
        (__ \ 'buildingName).readNullable[String] and
        (__ \ 'email).read[String] and
        (__ \ 'phone).read[String] and
        (__ \ 'loginName).read[String] and
        (__ \ 'password).read[String] and
        (__ \ 'favoriteCategoryId).readNullable[String])(CustomerJson.apply _).reads(json)
    }

    override def writes(o: Customer): JsValue = {
      JsObject(
        Seq(
          "id" -> (if (o.id.isDefined) JsString(o.id.value.toString) else JsNull),
          "name" -> JsString(o.name),
          "sexType" -> JsNumber(o.sexType.id),
          "zipCode" -> JsString(o.profile.postalAddress.zipCode.asString),
          "prefCode" -> JsNumber(o.profile.postalAddress.pref.id),
          "cityName" -> JsString(o.profile.postalAddress.cityName),
          "addressName" -> JsString(o.profile.postalAddress.addressName),
          "buildingName" -> o.profile.postalAddress.buildingName.map(JsString).getOrElse(JsNull),
          "email" -> JsString(o.profile.contact.email),
          "phone" -> JsString(o.profile.contact.phone),
          "loginName" -> JsString(o.config.loginName),
          "password" -> JsString(o.config.password),
          "favoriteCategoryId" -> o.config.favoriteCategoryId.map(e => JsString(e.value.toString)).getOrElse(JsNull)
        )
      )
    }

  }


}
