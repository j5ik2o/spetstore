package com.github.j5ik2o.spetstore.domain.model.purchase

import com.github.j5ik2o.spetstore.domain.infrastructure.support.EntityIOContextOnMemory
import com.github.j5ik2o.spetstore.domain.lifecycle.customer.CustomerRepository
import com.github.j5ik2o.spetstore.domain.lifecycle.item.ItemRepository
import com.github.j5ik2o.spetstore.domain.model.basic._
import com.github.j5ik2o.spetstore.domain.model.customer._
import com.github.j5ik2o.spetstore.domain.model.item.Item
import com.github.j5ik2o.spetstore.domain.model.item.ItemId
import com.github.j5ik2o.spetstore.domain.model.item.ItemTypeId
import com.github.j5ik2o.spetstore.domain.model.item.SupplierId
import org.joda.time.DateTime
import org.specs2.mutable.Specification
import scala.util.Success
import com.github.j5ik2o.spetstore.domain.lifecycle.IdentifierService

class OrderSpec extends Specification {

  val identifierService = IdentifierService()

  "order" should {
    val item = Item(
      id = ItemId(identifierService.generate),
      status = StatusType.Enabled,
      itemTypeId = ItemTypeId(identifierService.generate),
      name = "ぽち",
      description = None,
      price = BigDecimal(100),
      supplierId = SupplierId(identifierService.generate)
    )
    implicit val ctx = EntityIOContextOnMemory
    implicit val itemRepository = ItemRepository.ofMemory().storeEntity(item).get._1
    "add orderItem" in {
      val order = Order(
        id = OrderId(identifierService.generate),
        status = StatusType.Enabled,
        orderStatus = OrderStatus.Pending,
        orderDate = DateTime.now,
        userName = "Junichi Kato",
        shippingAddress = PostalAddress(
          ZipCode("100", "1000"),
          Pref.東京都,
          "目黒区下目黒",
          "1-1-1"
        ),
        orderItems = List.empty
      )
      val orderItem = OrderItem(1, StatusType.Enabled, item.id, 1)
      val newOrder = order.addOrderItem(orderItem)
      newOrder must_== order
      newOrder.orderItems.contains(orderItem) must beTrue
      newOrder.sizeOfOrderItems must_== 1
    }
    "remove orderItem" in {
      val orderItem = OrderItem(1, StatusType.Enabled, item.id, 1)
      val order = Order(
        id = OrderId(identifierService.generate),
        status = StatusType.Enabled,
        orderStatus = OrderStatus.Pending,
        orderDate = DateTime.now,
        userName = "Junichi Kato",
        shippingAddress = PostalAddress(
          ZipCode("100", "1000"),
          Pref.東京都,
          "目黒区下目黒",
          "1-1-1"
        ),
        orderItems = List(orderItem)
      )
      val newOrder = order.removeOrderItem(orderItem)
      newOrder must_== order
      newOrder.orderItems.contains(orderItem) must beFalse
      newOrder.sizeOfOrderItems must_== 0
    }
    "remove orderItem by index" in {
      val orderItem = OrderItem(1, StatusType.Enabled, item.id, 1)
      val order = Order(
        id = OrderId(identifierService.generate),
        status = StatusType.Enabled,
        orderStatus = OrderStatus.Pending,
        orderDate = DateTime.now,
        userName = "Junichi Kato",
        shippingAddress = PostalAddress(
          ZipCode("100", "1000"),
          Pref.東京都,
          "目黒区下目黒",
          "1-1-1"
        ),
        orderItems = List(orderItem)
      )
      val newOrder = order.removeOrderItemByIndex(0)
      newOrder must_== order
      newOrder.orderItems.contains(orderItem) must beFalse
      newOrder.sizeOfOrderItems must_== 0
    }
    "get totalPrice" in {
      val orderItem = OrderItem(1, StatusType.Enabled, item.id, 1)
      val order = Order(
        id = OrderId(identifierService.generate),
        status = StatusType.Enabled,
        orderStatus = OrderStatus.Pending,
        orderDate = DateTime.now,
        userName = "Junichi Kato",
        shippingAddress = PostalAddress(
          ZipCode("100", "1000"),
          Pref.東京都,
          "目黒区下目黒",
          "1-1-1"
        ),
        orderItems = List(orderItem)
      )
      order.totalPrice must_== Success(BigDecimal(100))
    }
    "apply from cart" in {
      val customer = Customer(
        id = CustomerId(identifierService.generate),
        status = StatusType.Enabled,
        name = "Junichi Kato",
        sexType = SexType.Female,
        profile = CustomerProfile(
          postalAddress = PostalAddress(
            ZipCode("100", "1000"),
            Pref.東京都,
            "目黒区下目黒",
            "1-1-1"
          ),
          contact = Contact("hoge@hoge.com", "00-0000-0000")
        ),
        config = CustomerConfig(
          loginName = "fugafuga",
          password = "hogehoge",
          favoriteCategoryId = None
        )
      )
      val cart = Cart(
        id = CartId(identifierService.generate),
        StatusType.Enabled,
        customerId = customer.id,
        cartItems = List(
          CartItem(1, StatusType.Enabled, item.id, 1, false)
        )
      )
      implicit val ar = CustomerRepository.ofMemory(Map(customer.id -> customer))
      implicit val ctx = EntityIOContextOnMemory
      val order = Order.fromCart(OrderId(identifierService.generate), cart).get
      order.orderItems.exists(e => e.itemId == item.id && e.quantity == 1) must beTrue
    }

  }

}
