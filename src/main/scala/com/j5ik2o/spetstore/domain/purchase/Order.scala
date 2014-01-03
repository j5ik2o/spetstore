package com.j5ik2o.spetstore.domain.purchase

import com.github.nscala_time.time.Imports._
import com.j5ik2o.spetstore.domain.account.AccountRepository
import com.j5ik2o.spetstore.domain.address.PostalAddress
import com.j5ik2o.spetstore.infrastructure.support.{EntityIOContext, Entity}
import scala.collection.mutable.ListBuffer
import scala.util.Try

/**
 * 注文を表すエンティティ。
 *
 * @param id 識別子
 * @param orderDate 注文日時
 * @param userName 購入者名
 * @param shippingAddress 出荷先の住所
 * @param orderItems [[com.j5ik2o.spetstore.domain.purchase.OrderItem]]のリスト
 */
case class Order
(id: OrderId = OrderId(),
 orderDate: DateTime,
 userName: String,
 shippingAddress: PostalAddress,
 orderItems: List[OrderItem])
  extends Entity[OrderId] {

  /**
   * [[com.j5ik2o.spetstore.domain.purchase.OrderItem]]の個数
   */
  val sizeOfOrderItems: Int = orderItems.size

  /**
   * [[com.j5ik2o.spetstore.domain.purchase.OrderItem]]の総数。
   */
  val quantityOfOrderItems = orderItems.foldLeft(0)(_ + _.quantity)

  /**
   * 合計を取得する。
   *
   * @return 合計
   */
  def totalPrice: BigDecimal =
    orderItems.map(_.subTotal).reduceLeft(_ + _)

  /**
   * この注文に[[com.j5ik2o.spetstore.domain.purchase.OrderItem]]を追加する。
   *
   * @param orderItem [[com.j5ik2o.spetstore.domain.purchase.OrderItem]]
   * @return 新しい[[com.j5ik2o.spetstore.domain.purchase.Order]]
   */
  def addOrderItem(orderItem: OrderItem): Order =
    copy(orderItems = orderItem :: orderItems)

  /**
   * この注文から[[com.j5ik2o.spetstore.domain.purchase.OrderItem]]を削除する。
   *
   * @param orderItem [[com.j5ik2o.spetstore.domain.purchase.OrderItem]]
   * @return 新しい[[com.j5ik2o.spetstore.domain.purchase.Order]]
   */
  def removeOrderItem(orderItem: OrderItem): Order =
    copy(orderItems = orderItems.filterNot(_ == orderItem))

  /**
   * この注文から指定したインデックスの[[com.j5ik2o.spetstore.domain.purchase.OrderItem]]を削除する。
   *
   * @param index インデックス
   * @return 新しい[[com.j5ik2o.spetstore.domain.purchase.Order]]
   */
  def removeOrderItemByIndex(index: Int): Order = {
    val lb = ListBuffer(orderItems: _*)
    lb.remove(index)
    copy(orderItems = lb.result())
  }

}

/**
 * コンパニオンオブジェクト。
 */
object Order {

  /**
   * [[com.j5ik2o.spetstore.domain.purchase.Cart]]から[[com.j5ik2o.spetstore.domain.purchase.Order]]を生成する。
   *
   * @param cart [[com.j5ik2o.spetstore.domain.purchase.Cart]]
   * @return [[com.j5ik2o.spetstore.domain.purchase.Order]]
   */
  def fromCart(cart: Cart)(implicit ar: AccountRepository, ctx: EntityIOContext): Try[Order] = Try {
    val account = cart.account.get
    val orderItems = cart.cartItems.map(OrderItem.fromCartItem)
    Order(OrderId(), DateTime.now, account.name, account.profile.postalAddress, orderItems)
  }

}
