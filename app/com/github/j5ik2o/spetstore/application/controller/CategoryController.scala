package com.github.j5ik2o.spetstore.application.controller

import javax.inject.{ Inject, Singleton }

import com.github.j5ik2o.spetstore.application.EntityIOContextProvider
import com.github.j5ik2o.spetstore.application.controller.json.{ CategoryJson, CategoryJsonSupport }
import com.github.j5ik2o.spetstore.domain.lifecycle.item.CategoryRepository
import com.github.j5ik2o.spetstore.domain.model.item.{ Category, CategoryId }
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService
//import io.swagger.annotations.Api
import play.api.mvc.{ Action, AnyContent }
import io.circe.generic.auto._

/**
 * [[Category]]のためのコントローラ。
 *
 * @param identifierService [[IdentifierService]]
 * @param entityIOContextProvider [[]]
 * @param repository
 */
//@Api(value = "/categories")
@Singleton
case class CategoryController @Inject() (
  identifierService: IdentifierService,
  entityIOContextProvider: EntityIOContextProvider,
  repository: CategoryRepository
)
    extends ControllerSupport[CategoryId, Category, CategoryJson] with CategoryJsonSupport {

  implicit val ctx = entityIOContextProvider.get

  def list: Action[AnyContent] = listAction

  def create: Action[CategoryJson] = createAction

  def get(id: Long): Action[AnyContent] = getAction(id)(CategoryId)

  def update(id: Long): Action[CategoryJson] = updateAction(id)(CategoryId)

  def delete(id: Long): Action[AnyContent] = deleteAction(id)(CategoryId)

}
