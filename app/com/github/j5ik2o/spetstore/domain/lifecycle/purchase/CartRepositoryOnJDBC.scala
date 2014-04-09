package com.github.j5ik2o.spetstore.domain.lifecycle.purchase

import com.github.j5ik2o.spetstore.domain.infrastructure.support.{EntityNotFoundException, Identifier, DaoSupport, RepositoryOnJDBC}
import com.github.j5ik2o.spetstore.domain.model.basic.StatusType
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerId
import com.github.j5ik2o.spetstore.domain.model.item.ItemId
import com.github.j5ik2o.spetstore.domain.model.purchase.{CartItemId, CartItem, Cart, CartId}
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

    override def convertToRecord(model: Cart): CartRecord = {
      CartRecord(
        id = model.id.value,
        status = model.status.id,
        customerId = model.customerId.value,
        version = model.version.getOrElse(1)
      )
    }

    override def convertToEntity(record: CartRecord): Cart = Cart(
      id = CartId(record.id),
      status = StatusType(record.status),
      customerId = CustomerId(record.customerId),
      cartItems = record.cartItems.map(CartItemRecordService.convertToEntity).toList,
      version = Some(record.version)
    )

    override def convertToPrimaryKey(id: Identifier[Long]): Long = id.value

    override def findById(id: Identifier[Long])(implicit s: DBSession): Try[Cart] = Try {
      CartRecord.joins(CartRecord.cartItemsRef).
        findBy(sqls.eq(mapper.defaultAlias.field(idName), convertToPrimaryKey(id))).
        map(convertToEntity).getOrElse(throw new EntityNotFoundException(s"$id"))
    }

    override def findAllWithLimitOffset(limit: Int, offset: Int)(implicit s: DBSession): Try[Seq[Cart]] = Try {
      CartRecord.joins(CartRecord.cartItemsRef).findAllWithLimitOffset(limit, offset).map(convertToEntity)
    }

  }

  private case class CartItemRecordService(cartId: CartId)
    extends DaoSupport[Long, CartItem, CartItemRecord] {

    override protected val mapper = CartItemRecord

    def findByCartId = Try {
      val alias = mapper.defaultAlias
      mapper.findAllBy(sqls.eq(alias.cartId, cartId.value)).map(convertToEntity)
    }

    override def convertToEntity(record: CartItemRecord): CartItem = CartItemRecordService.convertToEntity(record)

    override def convertToRecord(model: CartItem): CartItemRecord = CartItemRecordService.convertToRecord(cartId, model)

    override def convertToPrimaryKey(id: Long): Long = CartItemRecordService.convertToPrimaryKey(id)
  }

  private object CartItemRecordService {

    def convertToRecord(cartId: CartId, model: CartItem): CartItemRecord = {
      CartItemRecord(
        id = model.id.value,
        no = model.no,
        status = model.status.id,
        cartId = cartId.value,
        itemId = model.itemId.value,
        quantity = model.quantity,
        inStock = model.inStock,
        version = model.version.getOrElse(1)
      )
    }

    def convertToEntity(record: CartItemRecord): CartItem = CartItem(
      id = CartItemId(record.id),
      no = record.no,
      status = StatusType(record.status),
      itemId = ItemId(record.itemId),
      quantity = record.quantity,
      inStock = record.inStock,
      version = Some(record.version)
    )

    def convertToPrimaryKey(id: Long): Long = id

  }

  override def store(entity: Cart)(implicit ctx: Ctx): Try[(This, Cart)] = withDBSession(ctx) {
    implicit s =>
      CartRecordService.insertOrUpdate(entity.id, entity.version, entity).map {
        entity =>
          val cartItemRecordService = CartItemRecordService(entity.id)
          entity.cartItems.foreach {
            cartItem =>
              cartItemRecordService.insertOrUpdate(cartItem.no, cartItem.version, cartItem).get
          }
          (this.asInstanceOf[This], entity)
      }
  }

  override def deleteById(identifier: CartId)(implicit ctx: Ctx): Try[(This, Cart)] = withDBSession(ctx) {
    implicit s =>
      CartRecordService.findById(identifier).flatMap {
        entity =>
          val cartItemRecordService = CartItemRecordService(entity.id)
          entity.cartItems.foreach {
            cartItem =>
              cartItemRecordService.deleteById(cartItem.id.value).get
          }
          CartRecordService.deleteById(identifier).map {
            _ =>
              (this.asInstanceOf[This], entity)
          }
      }
  }

  override def resolveByOffsetWithLimit(offset: Int, limit: Int)(implicit ctx: Ctx): Try[Seq[Cart]] = withDBSession(ctx) {
    implicit s =>
      CartRecordService.findAllWithLimitOffset(limit, offset)
  }

  override def resolveById(identifier: CartId)(implicit ctx: Ctx): Try[Cart] = withDBSession(ctx) {
    implicit s =>
      CartRecordService.findById(identifier)
  }
}
