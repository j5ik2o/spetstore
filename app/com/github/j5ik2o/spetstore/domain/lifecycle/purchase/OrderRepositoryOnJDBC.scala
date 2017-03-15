package com.github.j5ik2o.spetstore.domain.lifecycle.purchase

import com.github.j5ik2o.spetstore.domain.model.basic._
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerId
import com.github.j5ik2o.spetstore.domain.model.item.ItemId
import com.github.j5ik2o.spetstore.domain.model.purchase._
import com.github.j5ik2o.spetstore.domain.support.support._
import com.github.j5ik2o.spetstore.infrastructure.db.{ OrderItemRecord, OrderDao, OrderItemDao, OrderRecord }

import scala.util.Try

class OrderRepositoryOnJDBC
    extends RepositoryOnJDBC[OrderId, Order] with OrderRepository {

  override type T = OrderRecord

  override protected lazy val mapper = OrderRecord

  override def resolveByOffsetWithLimit(offset: Int, limit: Int)(implicit ctx: Ctx): Try[Seq[Order]] = withDBSession(ctx) {
    implicit s =>
      OrderDao.findAllWithLimitOffset(offset, limit).map(_.map(convertToOrder))
  }

  override def resolveById(identifier: OrderId)(implicit ctx: Ctx): Try[Order] = withDBSession(ctx) {
    implicit s =>
      OrderDao.findById(identifier.value).map(convertToOrder)
  }

  override def deleteById(identifier: OrderId)(implicit ctx: Ctx): Try[(This, Order)] = withDBSession(ctx) {
    implicit s =>
      OrderDao.findById(identifier.value).flatMap {
        record =>
          val orderItemDao = OrderItemDao(record.id)
          record.orderItems.foreach {
            orderItem =>
              orderItemDao.deleteById(orderItem.no).get
          }
          OrderDao.deleteById(identifier.value).map {
            _ =>
              (this.asInstanceOf[This], convertToOrder(record))
          }
      }
  }

  override def store(entity: Order)(implicit ctx: Ctx): Try[(This, Order)] = withDBSession(ctx) {
    implicit s =>
      OrderDao.insertOrUpdate(entity.id.value, entity.version, convertToOrderRecord(entity)).map {
        record =>
          val orderItemRecordService = OrderItemDao(entity.id.value)
          entity.orderItems.foreach {
            orderItem =>
              orderItemRecordService.insertOrUpdate(orderItem.no, orderItem.version, convertToOrderItemRecord(entity.id, orderItem)).get
          }
          (this.asInstanceOf[This], convertToOrder(record))
      }
  }

  private def convertToOrder(record: OrderRecord): Order = Order(
    id = OrderId(record.id),
    status = StatusType(record.status),
    orderStatus = OrderStatus(record.orderStatus),
    orderDate = record.orderDateTime,
    customerId = CustomerId(record.customerId),
    customerName = record.customerName,
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
    orderItems = record.orderItems.map(convertToOrderItem).toList,
    version = Some(record.version)
  )

  private def convertToOrderRecord(model: Order): OrderRecord = OrderRecord(
    id = model.id.value,
    status = model.status.id,
    orderStatus = model.orderStatus.id,
    orderDateTime = model.orderDate,
    customerId = model.customerId.value,
    customerName = model.customerName,
    zipCode = model.shippingAddress.zipCode.asString,
    prefCode = model.shippingAddress.pref.id,
    cityName = model.shippingAddress.cityName,
    addressName = model.shippingAddress.addressName,
    buildingName = model.shippingAddress.buildingName,
    email = model.shippingContact.email,
    phone = model.shippingContact.phone,
    version = model.version.getOrElse(1)
  )

  private def convertToOrderItemRecord(_orderId: OrderId, orderItem: OrderItem): OrderItemRecord = OrderItemRecord(
    id = orderItem.id.value,
    status = orderItem.status.id,
    orderId = _orderId.value,
    no = orderItem.no,
    itemId = orderItem.itemId.value,
    quantity = orderItem.quantity,
    version = orderItem.version.getOrElse(1)
  )

  private def convertToOrderItem(record: OrderItemRecord): OrderItem = OrderItem(
    id = OrderItemId(record.id),
    status = StatusType(record.status),
    no = record.no,
    itemId = ItemId(record.itemId),
    quantity = record.quantity,
    version = Some(record.version)
  )

}
