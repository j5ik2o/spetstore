package com.github.j5ik2o.spetstore.application.controller

import com.github.j5ik2o.spetstore.application.EntityIOContextProvider
import com.github.j5ik2o.spetstore.domain.infrastructure.support.EntityNotFoundException
import com.github.j5ik2o.spetstore.domain.model.basic.Contact
import com.github.j5ik2o.spetstore.domain.model.basic.PostalAddress
import com.github.j5ik2o.spetstore.domain.model.basic.{Pref, ZipCode}
import com.github.j5ik2o.spetstore.domain.model.customer._
import com.google.inject.Inject
import java.util.UUID
import org.json4s._
import org.json4s.jackson.JsonMethods._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._
import scala.util.{Success, Failure}

class CustomerController @Inject()
(customerRepository: CustomerRepository,
 entityIOContextProvider: EntityIOContextProvider) extends ControllerSupport {

  implicit val ctx = entityIOContextProvider.get

  implicit val rds =
    (__ \ 'name).read[String] and
      (__ \ 'zipCode1).read[String] and
      (__ \ 'zipCode2).read[String] and
      (__ \ 'prefCode).read[Int] and
      (__ \ 'cityName).read[String] and
      (__ \ 'addressName).read[String] and
      (__ \ 'buildingName).readNullable[String] and
      (__ \ 'email).read[String] and
      (__ \ 'phone).read[String] and
      (__ \ 'loginName).read[String] and
      (__ \ 'password).read[String] tupled

  def create = Action {
    request =>
      request.body.asJson.map {
        json =>
          json.validate[(String, String, String, Int, String, String, Option[String], String, String, String, String)].map {
            case (name, zipCode1, zipCode2, prefCode, cityName, addressName, buildingName, email, phone, loginName, password) =>
              val customer = Customer(
                id = CustomerId(UUID.randomUUID()),
                status = CustomerStatus.Enabled,
                name = name,
                profile = CustomerProfile(
                  postalAddress = PostalAddress(
                    ZipCode(zipCode1, zipCode2),
                    Pref(prefCode),
                    cityName,
                    addressName,
                    buildingName
                  ),
                  contact = Contact(email, phone)
                ),
                config = CustomerConfig(
                  loginName = loginName,
                  password = password,
                  favoriteCategoryId = None
                )
              )
              customerRepository.storeEntity(customer).recoverWith {
                case ex => ex.printStackTrace(); Failure(ex)
              }.map {
                _ =>
                  Ok( s"""{"id": ${customer.id.value}}""")
              }.getOrElse(BadRequest("io error"))
          }.recoverTotal {
            e => BadRequest("Detected error:" + JsError.toFlatJson(e))
          }
      }.getOrElse {
        BadRequest("ng")
      }
  }


  def get(customerId: String) = Action {
    val id = CustomerId(UUID.fromString(customerId))
    customerRepository.resolveEntity(id).map {
      entity =>
        Ok(pretty(entity.asJValue))
    }.recoverWith {
      case ex: EntityNotFoundException =>
        Success(NotFound)
    }.getOrElse(InternalServerError)
  }

}
