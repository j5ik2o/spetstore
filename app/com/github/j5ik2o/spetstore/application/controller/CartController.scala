package com.github.j5ik2o.spetstore.application.controller

import com.github.j5ik2o.spetstore.application.EntityIOContextProvider
import com.github.j5ik2o.spetstore.application.controller.json.{CartItemJson, CartJson, CartJsonSupport}
import com.github.j5ik2o.spetstore.domain.lifecycle.purchase.CartRepository
import com.github.j5ik2o.spetstore.domain.model.item.ItemId
import com.github.j5ik2o.spetstore.domain.model.purchase.{Cart, CartId, CartItemId}
import com.github.j5ik2o.spetstore.domain.support.support.EntityNotFoundException
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService
import com.google.inject.Inject
import com.wordnik.swagger.annotations.Api
import play.api.libs.json.Json._
import play.api.mvc.Action

@Api(value = "/carts", description = "カート用API")
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

  def addCartItem(id: Long) = Action {
    request =>
      request.body.asJson.map {
        json =>
          json.validate[CartItemJson].fold(defaultErrorHandler, {
            validatedJson =>
              withTransaction {
                implicit ctx =>
                  val identifier = CartId(id)
                  repository.resolveById(identifier).flatMap {
                    cart =>
                      val newCart = cart.addCartItem(
                        CartItemId(identifierService.generate),
                        ItemId(validatedJson.itemId.toLong),
                        validatedJson.quantity,
                        validatedJson.inStock
                      )
                      repository.store(newCart).map(_._2).map {
                        entity =>
                          Ok(prettyPrint(toJson(entity)))
                      }.recover {
                        case ex =>
                          BadRequestForIOError(ex)
                      }
                  }.recover {
                    case ex: EntityNotFoundException =>
                      NotFoundForEntity(identifier)
                  }
              }.get
          })
      }.getOrElse(InternalServerError)
  }

  def removeCartItem(id: Long, cartItemId: Long) = Action {
    request =>
      withTransaction {
        implicit ctx =>
          withTransaction {
            implicit ctx =>
              val identifier = CartId(id)
              repository.resolveById(identifier).flatMap {
                cart =>
                  val newCart = cart.removeCartItemId(CartItemId(cartItemId))
                  repository.store(newCart).map(_._2).map {
                    entity =>
                      Ok(prettyPrint(toJson(entity)))
                  }.recover {
                    case ex =>
                      BadRequestForIOError(ex)
                  }
              }.recover {
                case ex: EntityNotFoundException =>
                  NotFoundForEntity(identifier)
              }
          }
      }.getOrElse(InternalServerError)
  }

}
