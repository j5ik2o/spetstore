package com.github.j5ik2o.spetstore.application.controller

import com.github.j5ik2o.spetstore.application.EntityIOContextProvider
import com.github.j5ik2o.spetstore.application.controller.json.{CartJson, CartJsonSupport}
import com.github.j5ik2o.spetstore.domain.lifecycle.purchase.CartRepository
import com.github.j5ik2o.spetstore.domain.model.purchase.{Cart, CartId}
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService
import com.google.inject.Inject

case class CartController @Inject()
(identifierService: IdentifierService,
 entityIOContextProvider: EntityIOContextProvider,
 repository: CartRepository)
  extends ControllerSupport[CartId, Cart, CartJson] with CartJsonSupport {

  implicit val ctx = entityIOContextProvider.get

  def list = listAction

  def create = createAction

  def get(id: Long) = getAction(id)(CartId)

  def update(id: Long) = updateAction(id)(CartId)

  def delete(id: Long) = deleteAction(id)(CartId)

}
