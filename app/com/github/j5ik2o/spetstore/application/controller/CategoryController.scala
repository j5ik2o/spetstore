package com.github.j5ik2o.spetstore.application.controller

import com.github.j5ik2o.spetstore.application.EntityIOContextProvider
import com.github.j5ik2o.spetstore.application.json.{CategoryJsonSupport, CategoryJson}
import com.github.j5ik2o.spetstore.domain.lifecycle.item.CategoryRepository
import com.github.j5ik2o.spetstore.domain.model.item.{Category, CategoryId}
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService
import com.google.inject.Inject

/**
 * [[Category]]のためのコントローラ。
 *
 * @param identifierService [[IdentifierService]]
 * @param entityIOContextProvider [[]]
 * @param repository
 */
case class CategoryController @Inject()
(identifierService: IdentifierService,
 entityIOContextProvider: EntityIOContextProvider,
 repository: CategoryRepository)
  extends ControllerSupport[CategoryId, Category, CategoryJson] with CategoryJsonSupport {

  implicit val ctx = entityIOContextProvider.get

  def list = listAction

  def create = createAction

  def get(id: Long) = getAction(id)(CategoryId)

  def update(id: Long) = updateAction(id)(CategoryId)

  def delete(id: Long) = deleteAction(id)(CategoryId)

}
