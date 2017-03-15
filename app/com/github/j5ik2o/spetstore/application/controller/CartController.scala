package com.github.j5ik2o.spetstore.application.controller

import javax.inject.{ Inject, Singleton }

import com.github.j5ik2o.spetstore.application.EntityIOContextProvider
import com.github.j5ik2o.spetstore.application.controller.json.{ CartItemJson, CartJson, CartJsonSupport }
import com.github.j5ik2o.spetstore.domain.lifecycle.purchase.CartRepository
import com.github.j5ik2o.spetstore.domain.model.item.ItemId
import com.github.j5ik2o.spetstore.domain.model.purchase.{ Cart, CartId, CartItemId }
import com.github.j5ik2o.spetstore.domain.support.support.{ EntityIOContext, EntityNotFoundException }
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService
import io.circe.generic.auto._
import io.circe.syntax._
import play.api.mvc.{ Action, AnyContent }

//@Api(value = "/carts")
@Singleton
case class CartController @Inject() (
  identifierService: IdentifierService,
  entityIOContextProvider: EntityIOContextProvider,
  repository: CartRepository
)
    extends ControllerSupport[CartId, Cart, CartJson] with CartJsonSupport {

  implicit val ctx: EntityIOContext = entityIOContextProvider.get

  def list: Action[AnyContent] = listAction

  def create: Action[CartJson] = createAction

  def get(id: Long): Action[AnyContent] = getAction(id)(CartId)

  def update(id: Long): Action[CartJson] = updateAction(id)(CartId)

  def delete(id: Long): Action[AnyContent] = deleteAction(id)(CartId)

  def addCartItem(id: Long): Action[CartItemJson] = Action(circe.json[CartItemJson]) {
    request =>
      withTransaction {
        implicit ctx =>
          val identifier = CartId(id)
          repository.resolveById(identifier).flatMap {
            cart =>
              val newCart = cart.addCartItem(
                CartItemId(identifierService.generate),
                ItemId(request.body.itemId),
                request.body.quantity,
                request.body.inStock
              )
              repository.store(newCart).map(_._2).map {
                entity =>
                  Ok(convertToJson(entity).asJson)
              }.recover {
                case ex =>
                  BadRequestForIOError(ex)
              }
          }.recover {
            case ex: EntityNotFoundException =>
              NotFoundForEntity(identifier)
          }
      }.getOrElse(InternalServerError)
  }

  def removeCartItem(id: Long, cartItemId: Long): Action[AnyContent] = Action {
    withTransaction {
      implicit ctx =>
        val identifier = CartId(id)
        repository.resolveById(identifier).flatMap {
          cart =>
            val newCart = cart.removeCartItemId(CartItemId(cartItemId))
            repository.store(newCart).map(_._2).map {
              entity =>
                Ok(convertToJson(entity).asJson)
            }.recover {
              case ex =>
                BadRequestForIOError(ex)
            }
        }.recover {
          case ex: EntityNotFoundException =>
            NotFoundForEntity(identifier)
        }
    }.getOrElse(InternalServerError)
  }

}
