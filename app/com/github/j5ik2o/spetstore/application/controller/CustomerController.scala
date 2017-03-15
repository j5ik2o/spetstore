package com.github.j5ik2o.spetstore.application.controller

import javax.inject.{ Inject, Singleton }

import com.github.j5ik2o.spetstore.application.EntityIOContextProvider
import com.github.j5ik2o.spetstore.application.controller.json.{ CustomerJson, CustomerJsonSupport }
import com.github.j5ik2o.spetstore.domain.lifecycle.customer.CustomerRepository
import com.github.j5ik2o.spetstore.domain.model.customer.{ Customer, CustomerId }
import com.github.j5ik2o.spetstore.domain.support.support.EntityIOContext
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService
//import io.swagger.annotations.Api
import play.api.mvc.{ Action, AnyContent }
import io.circe.generic.auto._

//@Api(value = "/customers")
@Singleton
case class CustomerController @Inject() (
  identifierService: IdentifierService,
  entityIOContextProvider: EntityIOContextProvider,
  repository: CustomerRepository
) extends ControllerSupport[CustomerId, Customer, CustomerJson]
    with CustomerJsonSupport {

  implicit val ctx: EntityIOContext = entityIOContextProvider.get

  def list: Action[AnyContent] = listAction

  def create: Action[CustomerJson] = createAction

  def get(id: Long): Action[AnyContent] = getAction(id)(CustomerId)

  def update(id: Long): Action[CustomerJson] = updateAction(id)(CustomerId)

  def delete(id: Long): Action[AnyContent] = deleteAction(id)(CustomerId)

}
