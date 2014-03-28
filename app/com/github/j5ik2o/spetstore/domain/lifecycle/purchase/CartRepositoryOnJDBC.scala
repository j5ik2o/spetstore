package com.github.j5ik2o.spetstore.domain.lifecycle.purchase

import com.github.j5ik2o.spetstore.domain.infrastructure.support.{Identifier, DaoSupport, RepositoryOnJDBC}
import com.github.j5ik2o.spetstore.domain.model.basic.StatusType
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerId
import com.github.j5ik2o.spetstore.domain.model.item.ItemId
import com.github.j5ik2o.spetstore.domain.model.purchase.{CartItem, Cart, CartId}
import com.github.j5ik2o.spetstore.infrastructure.db.{CartItemRecord, CartRecord}
import scala.util.Try
import scalikejdbc._, SQLInterpolation._

private[purchase]
class CartRepositoryOnJDBC
  extends RepositoryOnJDBC[CartId, Cart] with CartRepository {
  self =>

  override type T = CartRecord

  override protected val mapper = CartRecord

  private object CartRecordService
    extends DaoSupport[Identifier[Long], Cart, CartRecord] {

    override protected val mapper = self.mapper

    override protected def convertToRecord(model: Cart): CartRecord = {
      CartRecord(
        id = model.id.value,
        status = model.status.id,
        customerId = model.customerId.value
      )
    }

    override protected def convertToEntity(record: CartRecord): Cart = Cart(
      id = CartId(record.id),
      status = StatusType(record.status),
      customerId = CustomerId(record.customerId),
      cartItems = List.empty
    )

    override protected def convertToPrimaryKey(id: Identifier[Long]): Long = id.value
  }

  private case class CartItemRecordService(cartId: CartId)
    extends DaoSupport[Long, CartItem, CartItemRecord] {

    override protected val mapper = CartItemRecord

    def findByCartId = Try {
      val alias = mapper.defaultAlias
      mapper.findAllBy(sqls.eq(alias.cartId, cartId.value)).map(convertToEntity)
    }

    override protected def convertToRecord(model: CartItem): CartItemRecord = {
      CartItemRecord(
        no = model.no,
        status = model.status.id,
        cartId = this.cartId.value,
        itemId = model.itemId.value,
        quantity = model.quantity,
        inStock = model.inStock
      )
    }

    override protected def convertToEntity(record: CartItemRecord): CartItem = CartItem(
      no = record.no,
      status = StatusType(record.status),
      itemId = ItemId(record.itemId),
      quantity = record.quantity,
      inStock = record.inStock
    )

    override protected def convertToPrimaryKey(id: Long): Long = id
  }

  override def storeEntity(entity: Cart)(implicit ctx: Ctx): Try[(This, Cart)] = withDBSession(ctx) {
    implicit s =>
      CartRecordService.insertOrUpdate(entity.id, entity).map {
        result =>
          entity.cartItems.foreach {
            cartItem =>
              CartItemRecordService(result.id).insertOrUpdate(cartItem.no, cartItem).get
          }
          (this.asInstanceOf[This], result)
      }
  }

  override def deleteByIdentifier(identifier: CartId)(implicit ctx: Ctx): Try[(This, Cart)] = withDBSession(ctx) {
    implicit s =>
      CartRecordService.findById(identifier).flatMap {
        entity =>
          entity.cartItems.foreach {
            cartItem =>
              CartItemRecordService(entity.id).deleteById(cartItem.no).get
          }
          CartRecordService.deleteById(identifier).map {
            _ =>
              (this.asInstanceOf[This], entity)
          }
      }
  }

  override def resolveEntities(offset: Int, limit: Int)(implicit ctx: Ctx): Try[Seq[Cart]] = withDBSession(ctx) {
    implicit s =>
      CartRecordService.findAllWithLimitOffset(offset, limit).map {
        entities =>
          entities.map {
            entity =>
              CartItemRecordService(entity.id).findByCartId.map {
                _cartItems =>
                  entity.copy(cartItems = _cartItems)
              }.get
          }
      }
  }

  override def resolveEntity(identifier: CartId)(implicit ctx: Ctx): Try[Cart] = withDBSession(ctx) {
    implicit s =>
      CartRecordService.findById(identifier).map {
        entity =>
          CartItemRecordService(entity.id).findByCartId.map {
            _cartItems =>
              entity.copy(cartItems = _cartItems)
          }.get
      }
  }
}
