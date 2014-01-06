package com.github.j5ik2o.spetstore.domain.purchase

import com.github.j5ik2o.spetstore.domain.basic.PostalAddress
import com.github.j5ik2o.spetstore.domain.basic.{Pref, ZipCode}
import com.github.j5ik2o.spetstore.domain.infrastructure.json.OrderFormats._
import com.github.j5ik2o.spetstore.infrastructure.support.RepositoryOnJDBC
import java.util.UUID
import org.joda.time.DateTime
import org.json4s._
import org.json4s.DefaultReaders._
import org.json4s.DefaultWriters._
import org.json4s.jackson.JsonMethods._
import scalikejdbc.WrappedResultSet

class OrderRepositoryOnJDBC
  extends RepositoryOnJDBC[OrderId, Order] with OrderRepository {

  override def tableName: String = "order"

  override def columnNames: Seq[String] = Seq(
    "id",
    "status",
    "order_status",
    "user_name",
    "zip_code",
    "pref_code",
    "city_name",
    "address_name",
    "building_name",
    "order_items"
  )

  protected def convertResultSetToEntity(resultSet: WrappedResultSet): Order =
    Order(
      id = OrderId(UUID.fromString(resultSet.string("id"))),
      status = OrderStatus(resultSet.int("status")),
      orderDate = new DateTime(resultSet.timestamp("order_date")),
      userName = resultSet.string("user_name"),
      shippingAddress = PostalAddress(
        zipCode = ZipCode(resultSet.string("zip_code")),
        pref = Pref(resultSet.int("pref_code")),
        cityName = resultSet.string("city_name"),
        addressName = resultSet.string("address_name"),
        buildingName = resultSet.stringOpt("building_name")
      ),
      orderItems = parse(resultSet.string("order_items")).as[List[OrderItem]]
    )

  protected def convertEntityToValues(entity: Order): Seq[Any] = Seq(
    entity.id.value.toString,
    entity.status.id,
    entity.orderDate.toDate,
    entity.userName,
    entity.shippingAddress.zipCode.asString,
    entity.shippingAddress.pref.toString,
    entity.shippingAddress.cityName,
    entity.shippingAddress.addressName,
    entity.shippingAddress.buildingName,
    compact(entity.orderItems.toArray.asJValue)
  )

}
