package com.github.j5ik2o.spetstore.domain.infrastructure.json

import com.github.j5ik2o.spetstore.domain.model.basic.{Contact, Pref, ZipCode, PostalAddress}
import org.json4s._
import org.json4s.DefaultReaders._
import org.json4s.DefaultWriters._


object BasicFormats {

  implicit object ZipCodeFormat extends Reader[ZipCode] with Writer[ZipCode] {

    def read(value: JValue): ZipCode =
      ZipCode(value.as[String])

    def write(obj: ZipCode): JValue =
      JString(obj.asString)

  }

  implicit object PrefFormat extends Reader[Pref.Value] with Writer[Pref.Value] {

    def read(value: JValue): Pref.Value =
      Pref(value.as[Int])

    def write(obj: Pref.Value): JValue =
      JInt(obj.id)

  }

  implicit object PostalAddressFormat extends Reader[PostalAddress] with Writer[PostalAddress] {

    def read(value: JValue): PostalAddress =
      PostalAddress(
        (value \ "zipCode").as[ZipCode],
        (value \ "prefCode").as[Pref.Value],
        (value \ "cityName").as[String],
        (value \ "addressName").as[String],
        (value \ "buildingName").as[Option[String]]
      )

    def write(obj: PostalAddress): JValue =
      JObject(
        JField("zipCode", obj.zipCode.asJValue),
        JField("prefCode", obj.pref.asJValue),
        JField("cityName", JString(obj.cityName)),
        JField("addressName", JString(obj.addressName)),
        JField("buildingName", obj.buildingName.asJValue)
      )
  }

  implicit object ContactFormat extends Reader[Contact] with Writer[Contact] {

    def write(obj: Contact): JValue =
    JObject(
      JField("email", JString(obj.email)),
      JField("phone", JString(obj.phone))
    )

    def read(value: JValue): Contact = ???

  }

}
