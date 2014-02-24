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


  private def convertToEntity(customerJson: CustomerJson): Customer =
    Customer(
      id = CustomerId(UUID.randomUUID()),
      status = CustomerStatus.Enabled,
      name = customerJson.name,
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


  def create = Action {
    request =>
      request.body.asJson.map {
        _.validate[CustomerJson].map {
          customerJson =>
            val customer = convertToEntity(customerJson)
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
        import com.github.j5ik2o.spetstore.domain.infrastructure.json.CustomerFormats._
        Ok(pretty(entity.asJValue))
    }.recoverWith {
      case ex: EntityNotFoundException =>
        Success(NotFound)
    }.getOrElse(InternalServerError)
  }

}
