package com.github.j5ik2o.spetstore.application.controller

import com.github.j5ik2o.spetstore.application.EntityIOContextProvider
import com.github.j5ik2o.spetstore.application.controller.json.{ItemJson, ItemJsonSupport}
import com.github.j5ik2o.spetstore.domain.lifecycle.item.ItemRepository
import com.github.j5ik2o.spetstore.domain.model.item.{Item, ItemId}
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService
import com.google.inject.Inject

case class ItemController @Inject()(identifierService: IdentifierService,
                                    entityIOContextProvider: EntityIOContextProvider,
                                    repository: ItemRepository)
  extends ControllerSupport[ItemId, Item, ItemJson]
  with ItemJsonSupport {

  implicit val ctx = entityIOContextProvider.get

  def list = listAction

  def create = createAction

  def get(id: Long) = getAction(id)(ItemId)

  def update(id: Long) = updateAction(id)(ItemId)

  def delete(id: Long) = deleteAction(id)(ItemId)

}
