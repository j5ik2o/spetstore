package com.github.j5ik2o.spetstore.application.controller

import com.github.j5ik2o.spetstore.application.EntityIOContextProvider
import com.github.j5ik2o.spetstore.domain.infrastructure.support.EntityNotFoundException
import com.github.j5ik2o.spetstore.domain.model.basic._
import com.github.j5ik2o.spetstore.domain.model.customer._
import com.google.inject.Inject
import java.util.UUID
import play.api.libs.json._
import play.api.libs.json.Json._
import play.api.mvc._
import scala.util.Success
import com.github.j5ik2o.spetstore.application.json.CustomerJsonSupport
import com.github.j5ik2o.spetstore.domain.lifecycle.customer.CustomerRepository
import play.api.Logger
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerConfig
import play.api.libs.json.JsArray
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerId
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerProfile
import scala.util.Success
import com.github.j5ik2o.spetstore.domain.model.basic.PostalAddress
import com.github.j5ik2o.spetstore.domain.model.customer.Customer
import com.github.j5ik2o.spetstore.domain.model.basic.Contact

class CustomerController @Inject()
(customerRepository: CustomerRepository,
 entityIOContextProvider: EntityIOContextProvider)
  extends ControllerSupport with CustomerJsonSupport {

  implicit val ctx = entityIOContextProvider.get

  private def convertToEntity(customerJson: CustomerJson): Customer =
    Customer(
      id = CustomerId(UUID.randomUUID()),
      status = CustomerStatus.Enabled,
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

  def create = Action {
    request =>
      Logger.debug("create")
      request.body.asJson.map {
        e =>
          Logger.debug("check1")
          e.validate[CustomerJson].map {
            customerJson =>
              customerRepository.storeEntity(convertToEntity(customerJson)).map {
                case (_, customer) =>
                  OkForCreatedEntity(customer.id)
              }.recover {
                case ex =>
                  Logger.error("catch error", ex)
                  BadRequestForIOError
              }.get
          }.recoverTotal {
            error =>
              BadRequestForValidate(JsError.toFlatJson(error))
          }
      }.getOrElse(InternalServerError)
  }

  def get(customerId: String) = Action {
    val id = CustomerId(UUID.fromString(customerId))
    customerRepository.resolveEntity(id).map {
      entity =>
        Ok(prettyPrint(toJson(entity)))
    }.recoverWith {
      case ex: EntityNotFoundException =>
        Success(NotFoundForEntity(id))
    }.getOrElse(InternalServerError)
  }

  def list = Action {
    request =>
      val offset = request.getQueryString("offset").map(_.toInt).getOrElse(0)
      val limit = request.getQueryString("limit").map(_.toInt).getOrElse(100)
      customerRepository.resolveEntities(offset, limit).map {
        entities =>
          Ok(prettyPrint(JsArray(entities.map(toJson(_)))))
      }.getOrElse(InternalServerError)
  }

}
