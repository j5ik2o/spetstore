package com.github.j5ik2o.spetstore.domain.infrastructure.json

import BasicFormats._
import com.github.j5ik2o.spetstore.domain.model.basic.{SexType, StatusType, Contact, PostalAddress}
import com.github.j5ik2o.spetstore.domain.model.customer.{CustomerId, CustomerConfig, CustomerProfile, Customer}
import com.github.j5ik2o.spetstore.domain.model.item.CategoryId
import org.json4s.DefaultReaders._
import org.json4s.JsonAST.{JInt, JString, JField, JObject}
import org.json4s._

object CustomerFormats {

  implicit val categoryIdFormat = IdentifierFormats.categoryIdFormat

  implicit object ConfigFormat extends Reader[CustomerConfig] with Writer[CustomerConfig] {

    def read(value: JValue): CustomerConfig =
      CustomerConfig(
        (value \ "loginName").as[String],
        (value \ "password").as[String],
        (value \ "favoriteCategoryId").as[Option[Long]].map(e => CategoryId(e))
      )

    def write(obj: CustomerConfig): JValue =
      JObject(
        JField("loginName", JString(obj.loginName)),
        JField("password", JString(obj.password)),
        JField("favoriteCategoryId", obj.favoriteCategoryId.map(_.asJValue).getOrElse(JNull))
      )

  }

  implicit object ProfileFormat extends Reader[CustomerProfile] with Writer[CustomerProfile] {

    def read(value: JValue): CustomerProfile =
      CustomerProfile(
        (value \ "postalAddress").as[PostalAddress],
        (value \ "contact").as[Contact]
      )

    def write(obj: CustomerProfile): JValue =
      JObject(
        JField("postalAddress", obj.postalAddress.asJValue),
        JField("contact", obj.contact.asJValue)
      )
  }

  implicit object CustomerFormat extends Reader[Customer] with Writer[Customer] {

    def write(obj: Customer): org.json4s.JValue = {
      JObject(
        JField("id", JString(obj.id.value.toString)),
        JField("status", JInt(obj.status.id)),
        JField("name", JString(obj.name)),
        JField("sexType", JInt(obj.sexType.id)),
        JField("profile", obj.profile.asJValue),
        JField("config", obj.config.asJValue),
        JField("version", obj.version.map(e => JString(e.toString)).getOrElse(JNull))
      )
    }

    def read(value: org.json4s.JValue): Customer = Customer(
      id = CustomerId((value \ "id").as[String].toLong),
      status = StatusType((value \ "status").as[Int]),
      name = (value \ "name").as[String],
      sexType = SexType((value \ "sexType").as[Int]),
      profile = (value \ "profile").as[CustomerProfile],
      config = (value \ "config").as[CustomerConfig],
      version = (value \ "version").as[Option[String]].map(_.toLong)
    )
  }

}
