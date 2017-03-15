package com.github.j5ik2o.spetstore.application.controller

import javax.inject.{ Inject, Singleton }

import com.github.j5ik2o.spetstore.application.EntityIOContextProvider
import com.github.j5ik2o.spetstore.application.controller.json.{ ItemJson, ItemJsonSupport }
import com.github.j5ik2o.spetstore.domain.lifecycle.item.ItemRepository
import com.github.j5ik2o.spetstore.domain.model.item.{ Item, ItemId }
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService
import play.api.mvc.{ Action, AnyContent }
import io.circe.generic.auto._

@Singleton
case class ItemController @Inject() (
  identifierService: IdentifierService,
  entityIOContextProvider: EntityIOContextProvider,
  repository: ItemRepository
)
    extends ControllerSupport[ItemId, Item, ItemJson]
    with ItemJsonSupport {

  implicit val ctx = entityIOContextProvider.get

  def list: Action[AnyContent] = listAction

  def create: Action[ItemJson] = createAction

  def get(id: Long): Action[AnyContent] = getAction(id)(ItemId)

  def update(id: Long): Action[ItemJson] = updateAction(id)(ItemId)

  def delete(id: Long): Action[AnyContent] = deleteAction(id)(ItemId)

}
