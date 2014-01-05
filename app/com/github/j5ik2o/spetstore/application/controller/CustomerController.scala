package com.github.j5ik2o.spetstore.application.controller

import com.github.j5ik2o.spetstore.application.EntityIOContextProvider
import com.github.j5ik2o.spetstore.domain.address.Contact
import com.github.j5ik2o.spetstore.domain.address.PostalAddress
import com.github.j5ik2o.spetstore.domain.address.{Pref, ZipCode}
import com.github.j5ik2o.spetstore.domain.customer._
import com.github.j5ik2o.spetstore.infrastructure.support.EntityNotFoundException
import com.google.inject.Inject
import java.util.UUID
import org.json4s._
import org.json4s.jackson.JsonMethods._
import play.api.mvc._

class CustomerController @Inject()
(customerRepository: CustomerRepository,
 entityIOContextProvider: EntityIOContextProvider) extends ControllerSupport {

  implicit val ctx = entityIOContextProvider.get
  val customer = Customer(
    id = CustomerId(UUID.fromString("44f4338c-59a8-4a45-8eeb-a3872f11ea1d")),
    status = CustomerStatus.Enabled,
    name = "Junichi Kato",
    profile = CustomerProfile(
      postalAddress = PostalAddress(
        ZipCode("100", "1000"),
        Pref.東京都,
        "目黒区下目黒",
        "1-1-1"
      ),
      contact = Contact("hoge@hoge.com", "00-0000-0000")
    ),
    config = CustomerConfig(
      loginName = "fugafuga",
      password = "hogehoge",
      favoriteCategoryId = None
    )
  )

  def get(customerId: String) = Action {
    val id = CustomerId(UUID.fromString(customerId))
    val result = customerRepository.storeEntity(customer)
    result.get._1.resolveEntity(id).map {
      entity =>
        Ok(pretty(Extraction.decompose(entity)))
    }.recover {
      case ex: EntityNotFoundException =>
        Ok("")
    }.getOrElse(InternalServerError)
  }

}
