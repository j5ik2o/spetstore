package com.github.j5ik2o.spetstore.infrastructure.db

import org.joda.time.DateTime
import scalikejdbc._

case class CustomerRecord(
  id: Long,
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
  favoriteCategoryId: Option[Long] = None,
  version: Long
)

object CustomerRecord extends CRUDMapper[CustomerRecord] {

  override def defaultAlias = createAlias("customer")

  override def tableName: String = "customer"

  override def extract(
    rs: WrappedResultSet,
    n: ResultName[CustomerRecord]
  ) = CustomerRecord(
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
    favoriteCategoryId = rs.get(n.favoriteCategoryId),
    version = rs.get(n.version)
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
    'favoriteCategoryId -> record.favoriteCategoryId,
    'version -> record.version
  )
}

case class CategoryRecord(
  id: Long,
  status: Int,
  name: String,
  description: Option[String] = None,
  version: Long
)

object CategoryRecord extends CRUDMapper[CategoryRecord] {

  override def defaultAlias = createAlias("category")

  override def tableName: String = "category"

  override def extract(
    rs: WrappedResultSet,
    n: ResultName[CategoryRecord]
  ) = CategoryRecord(
    id = rs.get(n.id),
    status = rs.get(n.status),
    name = rs.get(n.name),
    description = rs.get(n.description),
    version = rs.get(n.version)
  )

  override def toNamedValues(record: CategoryRecord): Seq[(Symbol, Any)] = Seq(
    'id -> record.id,
    'status -> record.status,
    'name -> record.name,
    'description -> record.description,
    'version -> record.version
  )
}

case class InventoryRecord(
  id: Long,
  status: Int,
  itemId: Long,
  quantity: Int,
  version: Long
)

object InventoryRecord extends CRUDMapper[InventoryRecord] {

  override def defaultAlias = createAlias("inventory")

  override def tableName: String = "inventory"

  override def extract(
    rs: WrappedResultSet,
    n: ResultName[InventoryRecord]
  ) = InventoryRecord(
    id = rs.get(n.id),
    status = rs.get(n.status),
    itemId = rs.get(n.itemId),
    quantity = rs.get(n.quantity),
    version = rs.get(n.version)
  )

  override def toNamedValues(record: InventoryRecord): Seq[(Symbol, Any)] = Seq(
    'id -> record.id,
    'status -> record.status,
    'itemId -> record.itemId,
    'quantity -> record.quantity,
    'version -> record.version
  )

}

case class ItemRecord(
  id: Long,
  status: Int,
  itemTypeId: Long,
  name: String,
  description: Option[String] = None,
  price: BigDecimal,
  supplierId: Long,
  version: Long
)

object ItemRecord extends CRUDMapper[ItemRecord] {

  override def defaultAlias = createAlias("item")

  override def tableName: String = "item"

  override def extract(
    rs: WrappedResultSet,
    n: ResultName[ItemRecord]
  ) = ItemRecord(
    id = rs.get(n.id),
    status = rs.get(n.status),
    itemTypeId = rs.get(n.itemTypeId),
    name = rs.get(n.name),
    description = rs.get(n.description),
    price = rs.get(n.price),
    supplierId = rs.get(n.supplierId),
    version = rs.get(n.version)
  )

  override def toNamedValues(record: ItemRecord) = Seq(
    'id -> record.id,
    'status -> record.status,
    'itemTypeId -> record.itemTypeId,
    'name -> record.name,
    'description -> record.description,
    'price -> record.price,
    'supplierId -> record.supplierId,
    'version -> record.version
  )

}

case class ItemTypeRecord(
  id: Long,
  status: Int,
  categoryId: Long,
  name: String,
  description: Option[String] = None,
  version: Long
)

object ItemTypeRecord extends CRUDMapper[ItemTypeRecord] {

  override def defaultAlias = createAlias("item_type")

  override def tableName: String = "item_type"

  override def extract(
    rs: WrappedResultSet,
    n: ResultName[ItemTypeRecord]
  ): ItemTypeRecord = ItemTypeRecord(
    id = rs.get(n.id),
    status = rs.get(n.status),
    categoryId = rs.get(n.categoryId),
    name = rs.get(n.name),
    description = rs.get(n.description),
    version = rs.get(n.version)
  )

  override def toNamedValues(record: ItemTypeRecord): Seq[(Symbol, Any)] = Seq(
    'id -> record.id,
    'status -> record.status,
    'categoryId -> record.categoryId,
    'name -> record.name,
    'description -> record.description,
    'version -> record.version
  )

}

case class SupplierRecord(
  id: Long,
  status: Int,
  name: String,
  zipCode: String,
  prefCode: Int,
  cityName: String,
  addressName: String,
  buildingName: Option[String],
  email: String,
  phone: String,
  version: Long
)

object SupplierRecord extends CRUDMapper[SupplierRecord] {

  override def defaultAlias = createAlias("supplier")

  override def tableName: String = "supplier"

  override def extract(
    rs: WrappedResultSet,
    n: ResultName[SupplierRecord]
  ): SupplierRecord = SupplierRecord(
    id = rs.get(n.id),
    status = rs.get(n.status),
    name = rs.get(n.name),
    zipCode = rs.get(n.zipCode),
    prefCode = rs.get(n.prefCode),
    cityName = rs.get(n.cityName),
    addressName = rs.get(n.addressName),
    buildingName = rs.get(n.buildingName),
    email = rs.get(n.email),
    phone = rs.get(n.phone),
    version = rs.get(n.version)
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
    'phone -> record.phone,
    'version -> record.version
  )
}

case class CartRecord(
  id: Long,
  status: Int,
  customerId: Long,
  cartItems: Seq[CartItemRecord] = Nil,
  version: Long
)

object CartRecord extends CRUDMapper[CartRecord] {

  override def defaultAlias = createAlias("cart")

  override def tableName: String = "cart"

  override def extract(
    rs: WrappedResultSet,
    n: ResultName[CartRecord]
  ): CartRecord = CartRecord(
    id = rs.get(n.id),
    status = rs.get(n.status),
    customerId = rs.get(n.customerId),
    version = rs.get(n.version)
  )

  override def toNamedValues(record: CartRecord): Seq[(Symbol, Any)] = Seq(
    'id -> record.id,
    'status -> record.status,
    'customerId -> record.customerId,
    'version -> record.version
  )

  val cartItemsRef = hasMany[CartItemRecord](
    // association's SkinnyMapper and alias
    many = CartItemRecord -> CartItemRecord.cartItemsAlias,
    // defines join condition by using aliases
    on = (c, ci) => sqls.eq(c.id, ci.cartId),
    // function to merge associations to main entity
    merge = (cart, cartItems) => cart.copy(cartItems = cartItems)
  )

}

case class CartItemRecord(
  id: Long,
  status: Int,
  cartId: Long,
  no: Int,
  itemId: Long,
  quantity: Int,
  inStock: Boolean,
  version: Long
)

object CartItemRecord extends CRUDMapper[CartItemRecord] {

  val cartItemsAlias = createAlias("cart_items")

  override def defaultAlias = createAlias("cart_item")

  override def tableName: String = "cart_item"

  override def extract(
    rs: WrappedResultSet,
    n: ResultName[CartItemRecord]
  ) = CartItemRecord(
    id = rs.get(n.id),
    status = rs.get(n.status),
    cartId = rs.get(n.cartId),
    no = rs.get(n.no),
    itemId = rs.get(n.itemId),
    quantity = rs.get(n.quantity),
    inStock = rs.get(n.inStock),
    version = rs.get(n.version)
  )

  override def toNamedValues(record: CartItemRecord): Seq[(Symbol, Any)] = Seq(
    'id -> record.id,
    'status -> record.status,
    'cartId -> record.cartId,
    'no -> record.no,
    'itemId -> record.itemId,
    'quantity -> record.quantity,
    'inStock -> record.inStock,
    'version -> record.version
  )

}

case class OrderRecord(
  id: Long,
  status: Int,
  orderStatus: Int,
  orderDateTime: DateTime,
  customerId: Long,
  customerName: String,
  zipCode: String,
  prefCode: Int,
  cityName: String,
  addressName: String,
  buildingName: Option[String],
  email: String,
  phone: String,
  orderItems: Seq[OrderItemRecord] = Nil,
  version: Long
)

object OrderRecord extends CRUDMapper[OrderRecord] {

  override def defaultAlias = createAlias("order")

  override def tableName: String = "order"

  override def extract(
    rs: WrappedResultSet,
    n: ResultName[OrderRecord]
  ) = OrderRecord(
    id = rs.get(n.id),
    status = rs.get(n.status),
    orderStatus = rs.get(n.orderStatus),
    orderDateTime = rs.get(n.orderDateTime),
    customerId = rs.get(n.customerId),
    customerName = rs.get(n.customerName),
    zipCode = rs.get(n.zipCode),
    prefCode = rs.get(n.prefCode),
    cityName = rs.get(n.cityName),
    addressName = rs.get(n.addressName),
    buildingName = rs.get(n.buildingName),
    email = rs.get(n.email),
    phone = rs.get(n.phone),
    version = rs.get(n.version)
  )

  override def toNamedValues(record: OrderRecord): Seq[(Symbol, Any)] = Seq(
    'id -> record.id,
    'status -> record.status,
    'orderStatus -> record.orderStatus,
    'orderDateTime -> record.orderDateTime,
    'customerId -> record.customerId,
    'customerName -> record.customerName,
    'zipCode -> record.zipCode,
    'prefCode -> record.prefCode,
    'cityName -> record.cityName,
    'addressName -> record.addressName,
    'buildingName -> record.buildingName,
    'email -> record.email,
    'phone -> record.phone,
    'version -> record.version
  )

  val orderItemsRef = hasMany[OrderItemRecord](
    // association's SkinnyMapper and alias
    many = OrderItemRecord -> OrderItemRecord.orderItemsAlias,
    // defines join condition by using aliases
    on = (o, oi) => sqls.eq(o.id, oi.orderId),
    // function to merge associations to main entity
    merge = (order, orderItems) => order.copy(orderItems = orderItems)
  )

}

case class OrderItemRecord(
  id: Long,
  status: Int,
  orderId: Long,
  no: Int,
  itemId: Long,
  quantity: Int,
  version: Long
)

object OrderItemRecord extends CRUDMapper[OrderItemRecord] {

  val orderItemsAlias = createAlias("order_items")

  override def defaultAlias = createAlias("order_item")

  override def tableName: String = "order_item"

  override def extract(
    rs: WrappedResultSet,
    n: ResultName[OrderItemRecord]
  ): OrderItemRecord = OrderItemRecord(
    id = rs.get(n.id),
    status = rs.get(n.status),
    orderId = rs.get(n.orderId),
    no = rs.get(n.no),
    itemId = rs.get(n.itemId),
    quantity = rs.get(n.quantity),
    version = rs.get(n.version)
  )

  override def toNamedValues(record: OrderItemRecord): Seq[(Symbol, Any)] = Seq(
    'id -> record.id,
    'status -> record.status,
    'orderId -> record.orderId,
    'no -> record.no,
    'itemId -> record.itemId,
    'quantity -> record.quantity,
    'version -> record.version
  )

}
