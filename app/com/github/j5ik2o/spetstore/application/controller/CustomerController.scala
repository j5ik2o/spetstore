package com.github.j5ik2o.spetstore.application.controller

import com.github.j5ik2o.spetstore.application.EntityIOContextProvider
import com.github.j5ik2o.spetstore.application.json.{CustomerJson, CustomerJsonSupport}
import com.github.j5ik2o.spetstore.domain.lifecycle.customer.CustomerRepository
import com.github.j5ik2o.spetstore.domain.model.customer.{Customer, CustomerId}
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService
import com.google.inject.Inject

case class CustomerController @Inject()
(identifierService: IdentifierService,
 entityIOContextProvider: EntityIOContextProvider,
 repository: CustomerRepository)
  extends ControllerSupport[CustomerId, Customer, CustomerJson]
  with CustomerJsonSupport {

  implicit val ctx = entityIOContextProvider.get

  def list = listAction

  def create = createAction

  def get(id: Long) = getAction(id)(CustomerId)

  def update(id: Long) = updateAction(id)(CustomerId)

  def delete(id: Long) = deleteAction(id)(CustomerId)

}
