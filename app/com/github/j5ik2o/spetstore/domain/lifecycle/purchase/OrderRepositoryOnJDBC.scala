package com.github.j5ik2o.spetstore.domain.lifecycle.purchase

import com.github.j5ik2o.spetstore.domain.infrastructure.support._
import com.github.j5ik2o.spetstore.domain.model.basic._
import com.github.j5ik2o.spetstore.domain.model.item.ItemId
import com.github.j5ik2o.spetstore.domain.model.purchase._
import com.github.j5ik2o.spetstore.infrastructure.db.{OrderItemRecord, OrderRecord}
import scala.util.Try
import scalikejdbc.DBSession
import scalikejdbc.SQLInterpolation._

class OrderRepositoryOnJDBC
  extends RepositoryOnJDBC[OrderId, Order] with OrderRepository {
  self =>

  override type T = OrderRecord

  override protected val mapper = OrderRecord

  private object OrderRecordService
    extends DaoSupport[Identifier[Long], Order, OrderRecord] {

    override protected val mapper = self.mapper

    override def convertToEntity(record: OrderRecord): Order = Order(
      id = OrderId(record.id),
      status = StatusType(record.status),
      orderStatus = OrderStatus(record.orderStatus),
      orderDate = record.orderDateTime,
      userName = record.userName,
      shippingAddress = PostalAddress(
        zipCode = ZipCode(record.zipCode),
        pref = Pref(record.prefCode),
        cityName = record.cityName,
        addressName = record.addressName,
        buildingName = record.buildingName
      ),
      shippingContact = Contact(
        email = record.email,
        phone = record.phone
      ),
      orderItems = record.orderItems.map(OrderItemRecordService.convertToEntity).toList
    )

    override def convertToRecord(model: Order): OrderRecord = OrderRecord(
      id = model.id.value,
      status = model.status.id,
      orderStatus = model.orderStatus.id,
      orderDateTime = model.orderDate,
      userName = model.userName,
      zipCode = model.shippingAddress.zipCode.asString,
      prefCode = model.shippingAddress.pref.id,
      cityName = model.shippingAddress.cityName,
      addressName = model.shippingAddress.addressName,
      buildingName = model.shippingAddress.buildingName,
      email = model.shippingContact.email,
      phone = model.shippingContact.phone
    )

    override def convertToPrimaryKey(id: Identifier[Long]): Long = id.value

    override def findById(id: Identifier[Long])(implicit s: DBSession): Try[Order] = Try {
      OrderRecord.joins(OrderRecord.orderItemsRef).
        findBy(sqls.eq(mapper.defaultAlias.field(idName), convertToPrimaryKey(id))).
        map(convertToEntity).getOrElse(throw new EntityNotFoundException(s"$id"))
    }

    override def findAllWithLimitOffset(limit: Int, offset: Int)(implicit s: DBSession): Try[Seq[Order]] = Try {
      OrderRecord.joins(OrderRecord.orderItemsRef).findAllWithLimitOffset(limit, offset).map(convertToEntity)
    }

  }


  private case class OrderItemRecordService(orderId: OrderId)
    extends DaoSupport[Long, OrderItem, OrderItemRecord] {

    override protected val mapper = OrderItemRecord

    def findByOrderId = Try {
      val alias = mapper.defaultAlias
      mapper.findAllBy(sqls.eq(alias.orderId, orderId.value)).map(convertToEntity)
    }

    override def convertToEntity(record: OrderItemRecord): OrderItem = OrderItemRecordService.convertToEntity(record)

    override def convertToRecord(model: OrderItem): OrderItemRecord = OrderItemRecordService.convertToRecord(orderId, model)

    override def convertToPrimaryKey(id: Long): Long = OrderItemRecordService.convertToPrimaryKey(id)
  }

  private object OrderItemRecordService {

    def convertToPrimaryKey(identifier: Long): Long = identifier

    def convertToRecord(_orderId: OrderId, orderItem: OrderItem): OrderItemRecord = OrderItemRecord(
      id = orderItem.id.value,
      status = orderItem.status.id,
      orderId = _orderId.value,
      no = orderItem.no,
      itemId = orderItem.itemId.value,
      quantity = orderItem.quantity
    )

    def convertToEntity(record: OrderItemRecord): OrderItem = OrderItem(
      id = OrderItemId(record.id),
      status = StatusType(record.status),
      no = record.no,
      itemId = ItemId(record.itemId),
      quantity = record.quantity
    )

  }


  override def resolveEntities(offset: Int, limit: Int)(implicit ctx: Ctx): Try[Seq[Order]] = withDBSession(ctx) {
    implicit s =>
      OrderRecordService.findAllWithLimitOffset(offset, limit)
  }

  override def resolveEntity(identifier: OrderId)(implicit ctx: Ctx): Try[Order] = withDBSession(ctx) {
    implicit s =>
      OrderRecordService.findById(identifier)
  }


  override def deleteByIdentifier(identifier: OrderId)(implicit ctx: Ctx): Try[(This, Order)] = withDBSession(ctx) {
    implicit s =>
      OrderRecordService.findById(identifier).flatMap {
        entity =>
          val cartItemRecordService = OrderItemRecordService(entity.id)
          entity.orderItems.foreach {
            orderItem =>
              cartItemRecordService.deleteById(orderItem.no).get
          }
          OrderRecordService.deleteById(identifier).map {
            _ =>
              (this.asInstanceOf[This], entity)
          }
      }
  }


  override def storeEntity(entity: Order)(implicit ctx: Ctx): Try[(This, Order)] = withDBSession(ctx) {
    implicit s =>
      OrderRecordService.insertOrUpdate(entity.id, entity).map {
        entity =>
          val orderItemRecordService = OrderItemRecordService(entity.id)
          entity.orderItems.foreach {
            orderItem =>
              orderItemRecordService.insertOrUpdate(orderItem.no, orderItem).get
          }
          (this.asInstanceOf[This], entity)
      }
  }


}
