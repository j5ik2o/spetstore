package com.github.j5ik2o.spetstore.infrastructure.db

import com.github.j5ik2o.spetstore.domain.infrastructure.db.CRUDMapper
import org.joda.time.DateTime
import scalikejdbc._, SQLInterpolation._

case class CustomerRecord
(id: Long,
 status: Int,
 name: String,
 sexType: Int,
 zipCode: String,
 prefCode: Int,
 cityName: String,
 addressName: String,
 buildingName: Option[String],
 email: String,
 phone: String,
 loginName: String,
 password: String,
 favoriteCategoryId: Option[Long] = None)

object CustomerRecord extends CRUDMapper[CustomerRecord] {

  override def defaultAlias = createAlias("c")

  override def tableName: String = "customer"

  override def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[CustomerRecord]) = CustomerRecord(
    id = rs.get(n.id),
    status = rs.get(n.status),
    name = rs.get(n.name),
    sexType = rs.get(n.sexType),
    zipCode = rs.get(n.zipCode),
    prefCode = rs.get(n.prefCode),
    cityName = rs.get(n.cityName),
    addressName = rs.get(n.addressName),
    buildingName = rs.get(n.buildingName),
    email = rs.get(n.email),
    phone = rs.get(n.phone),
    loginName = rs.get(n.loginName),
    password = rs.get(n.password),
    favoriteCategoryId = rs.get(n.favoriteCategoryId)
  )

  override def toNamedValues(record: CustomerRecord): Seq[(Symbol, Any)] = Seq(
    'id -> record.id,
    'status -> record.status,
    'name -> record.name,
    'sexType -> record.sexType,
    'zipCode -> record.zipCode,
    'prefCode -> record.prefCode,
    'cityName -> record.cityName,
    'addressName -> record.addressName,
    'buildingName -> record.buildingName,
    'email -> record.email,
    'phone -> record.phone,
    'loginName -> record.loginName,
    'password -> record.password,
    'favoriteCategoryId -> record.favoriteCategoryId
  )
}

case class CategoryRecord
(id: Long,
 status: Int,
 name: String,
 description: Option[String] = None)

object CategoryRecord extends CRUDMapper[CategoryRecord] {

  override def defaultAlias = createAlias("c")

  override def tableName: String = "category"

  override def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[CategoryRecord]) = CategoryRecord(
    id = rs.get(n.id),
    status = rs.get(n.status),
    name = rs.get(n.name),
    description = rs.get(n.description)
  )

  override def toNamedValues(record: CategoryRecord): Seq[(Symbol, Any)] = Seq(
    'id -> record.id,
    'status -> record.status,
    'name -> record.name,
    'description -> record.description
  )
}

case class InventoryRecord
(id: Long,
 status: Int,
 itemId: Long,
 quantity: Int)

object InventoryRecord extends CRUDMapper[InventoryRecord] {

  override def defaultAlias = createAlias("i")

  override def tableName: String = "inventory"

  override def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[InventoryRecord]) = InventoryRecord(
    id = rs.get(n.id),
    status = rs.get(n.status),
    itemId = rs.get(n.itemId),
    quantity = rs.get(n.quantity)
  )

  override def toNamedValues(record: InventoryRecord): Seq[(Symbol, Any)] = Seq(
    'id -> record.id,
    'status -> record.status,
    'itemId -> record.itemId,
    'quantity -> record.quantity
  )

}

case class ItemRecord
(id: Long,
 status: Int,
 itemTypeId: Long,
 name: String,
 description: Option[String] = None,
 price: BigDecimal,
 supplierId: Long)

object ItemRecord extends CRUDMapper[ItemRecord] {

  override def defaultAlias = createAlias("i")

  override def tableName: String = "item"

  override def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[ItemRecord]) = ItemRecord(
    id = rs.get(n.id),
    status = rs.get(n.status),
    itemTypeId = rs.get(n.itemTypeId),
    name = rs.get(n.name),
    description = rs.get(n.description),
    price = rs.get(n.price),
    supplierId = rs.get(n.supplierId)
  )

  override def toNamedValues(record: ItemRecord) = Seq(
    'id -> record.id,
    'status -> record.status,
    'itemTypeId -> record.itemTypeId,
    'name -> record.name,
    'description -> record.description,
    'price -> record.price,
    'supplierId -> record.supplierId
  )

}

case class ItemTypeRecord
(id: Long,
 status: Int,
 categoryId: Long,
 name: String,
 description: Option[String] = None)

object ItemTypeRecord extends CRUDMapper[ItemTypeRecord] {

  override def defaultAlias = createAlias("it")

  override def tableName: String = "item_type"

  override def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[ItemTypeRecord]): ItemTypeRecord = ItemTypeRecord(
    id = rs.get(n.id),
    status = rs.get(n.status),
    categoryId = rs.get(n.categoryId),
    name = rs.get(n.name),
    description = rs.get(n.description)
  )

  override def toNamedValues(record: ItemTypeRecord): Seq[(Symbol, Any)] = Seq(
    'id -> record.id,
    'status -> record.status,
    'categoryId -> record.categoryId,
    'name -> record.name,
    'description -> record.description
  )

}

case class SupplierRecord
(id: Long,
 status: Int,
 name: String,
 zipCode: String,
 prefCode: Int,
 cityName: String,
 addressName: String,
 buildingName: Option[String],
 email: String,
 phone: String)

object SupplierRecord extends CRUDMapper[SupplierRecord] {

  override def defaultAlias = createAlias("s")

  override def tableName: String = "supplier"

  override def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[SupplierRecord]): SupplierRecord = SupplierRecord(
    id = rs.get(n.id),
    status = rs.get(n.status),
    name = rs.get(n.name),
    zipCode = rs.get(n.zipCode),
    prefCode = rs.get(n.prefCode),
    cityName = rs.get(n.cityName),
    addressName = rs.get(n.addressName),
    buildingName = rs.get(n.buildingName),
    email = rs.get(n.email),
    phone = rs.get(n.phone)
  )

  override def toNamedValues(record: SupplierRecord): Seq[(Symbol, Any)] = Seq(
    'id -> record.id,
    'status -> record.status,
    'name -> record.name,
    'zipCode -> record.zipCode,
    'prefCode -> record.prefCode,
    'cityName -> record.cityName,
    'addressName -> record.addressName,
    'buildingName -> record.buildingName,
    'email -> record.email,
    'phone -> record.phone
  )
}


case class CartRecord(id: Long, status: Int, customerId: Long)

object CartRecord extends CRUDMapper[CartRecord] {

  override def defaultAlias = createAlias("c")

  override def tableName: String = "cart"

  override def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[CartRecord]): CartRecord = CartRecord(
    id = rs.get(n.id),
    status = rs.get(n.status),
    customerId = rs.get(n.customerId)
  )

  override def toNamedValues(record: CartRecord): Seq[(Symbol, Any)] = Seq(
    'id -> record.id,
    'status -> record.status,
    'customerId -> record.customerId
  )
}

case class CartItemRecord(no: Long, status: Int, cartId: Long, itemId: Long, quantity: Int, inStock: Boolean)

object CartItemRecord extends CRUDMapper[CartItemRecord] {

  override def defaultAlias = createAlias("ci")

  override def tableName: String = "cart_item"

  override def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[CartItemRecord])  = CartItemRecord(
    no = rs.get(n.no),
    status = rs.get(n.status),
    cartId = rs.get(n.cartId),
    itemId = rs.get(n.itemId),
    quantity = rs.get(n.quantity),
    inStock = rs.get(n.inStock)
  )

  override def toNamedValues(record: CartItemRecord): Seq[(Symbol, Any)] = Seq(
    'no -> record.no,
    'status -> record.status,
    'cartId -> record.cartId,
    'itemId -> record.itemId,
    'quantity -> record.quantity,
    'inStock -> record.inStock
  )

}

case class OrderRecord
(id: Long,
 status: Int,
 orderDateTime: DateTime,
 userName: String,
 zipCode: String,
 prefCode: Int,
 cityName: String,
 addressName: String,
 buildingName: Option[String],
 email: String,
 phone: String)

object OrderRecord extends CRUDMapper[OrderRecord] {

  override def defaultAlias = createAlias("o")

  override def tableName: String = "order"

  override def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[OrderRecord]) = OrderRecord(
    id = rs.get(n.id),
    status = rs.get(n.status),
    orderDateTime = rs.get(n.orderDateTime),
    userName = rs.get(n.userName),
    zipCode = rs.get(n.zipCode),
    prefCode = rs.get(n.prefCode),
    cityName = rs.get(n.cityName),
    addressName = rs.get(n.addressName),
    buildingName = rs.get(n.buildingName),
    email = rs.get(n.email),
    phone = rs.get(n.phone)
  )

  override def toNamedValues(record: OrderRecord): Seq[(Symbol, Any)] = Seq(
    'id -> record.id,
    'status -> record.status,
    'orderDateTime -> record.orderDateTime,
    'zipCode -> record.zipCode,
    'prefCode -> record.prefCode,
    'cityName -> record.cityName,
    'addressName -> record.addressName,
    'buildingName -> record.buildingName,
    'email -> record.email,
    'phone -> record.phone
  )

}

case class OrderItemRecord
(id: Long, status: Int, itemId: Long, quantity: Int)


object OrderItemRecord extends CRUDMapper[OrderItemRecord] {

  override def defaultAlias = createAlias("oi")

  override def tableName: String = "order_item"

  override def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[OrderItemRecord]): OrderItemRecord = ???

  override def toNamedValues(record: OrderItemRecord): Seq[(Symbol, Any)] = ???

}
