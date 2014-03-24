package com.github.j5ik2o.spetstore.domain.lifecycle.purchase

import com.github.j5ik2o.spetstore.domain.infrastructure.support.RepositoryOnJDBC
import com.github.j5ik2o.spetstore.domain.model.basic.PostalAddress
import com.github.j5ik2o.spetstore.domain.model.basic.{Pref, ZipCode}
import com.github.j5ik2o.spetstore.domain.model.purchase.{OrderStatus, OrderItem, Order, OrderId}
import java.util.UUID
import org.joda.time.DateTime
import org.json4s.DefaultReaders._
import org.json4s._
import org.json4s.jackson.JsonMethods._
import scalikejdbc._, SQLInterpolation._
import com.github.j5ik2o.spetstore.domain.infrastructure.json.OrderFormats._

class OrderRepositoryOnJDBC
  extends RepositoryOnJDBC[OrderId, Order] with OrderRepository {

  class Dao extends AbstractDao[Order] {

    override def tableName: String = "order"

    def toNamedValues(entity: Order): Seq[(Symbol, Any)] = Seq(
      'id -> entity.id.value.toString,
      'status -> entity.status.id,
      'orderDate -> entity.orderDate.toDate,
      'userName -> entity.userName,
      'zipCode -> entity.shippingAddress.zipCode.asString,
      'prefCode -> entity.shippingAddress.pref.id,
      'cityName -> entity.shippingAddress.cityName,
      'addressName -> entity.shippingAddress.addressName,
      'buildingName -> entity.shippingAddress.buildingName,
      'orderItems -> compact(JArray(entity.orderItems.toList.map(_.asJValue)))
    )

    def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[Order]): Order = {
      Order(
        id = OrderId(UUID.fromString(rs.string("id"))),
        status = OrderStatus(rs.int("status")),
        orderDate = new DateTime(rs.timestamp("order_date")),
        userName = rs.string("user_name"),
        shippingAddress = PostalAddress(
          zipCode = ZipCode(rs.string("zip_code")),
          pref = Pref(rs.int("pref_code")),
          cityName = rs.string("city_name"),
          addressName = rs.string("address_name"),
          buildingName = rs.stringOpt("building_name")
        ),
        orderItems = parse(rs.string("order_items")).as[List[OrderItem]]
      )
    }

  }

  override protected def createDao = new Dao

}
