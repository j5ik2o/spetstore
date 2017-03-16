package com.github.j5ik2o.spetstore.domain.model.purchase

import com.github.j5ik2o.spetstore.domain.support.support.{ EntityIOContext, Entity }
import com.github.j5ik2o.spetstore.domain.lifecycle.customer.CustomerRepository
import com.github.j5ik2o.spetstore.domain.lifecycle.item.ItemRepository
import com.github.j5ik2o.spetstore.domain.model.basic.{ Contact, StatusType, PostalAddress }
import com.github.j5ik2o.spetstore.domain.model.customer.CustomerId
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService
import com.github.nscala_time.time.Imports._
import scala.collection.mutable.ListBuffer
import scala.util.Try

/**
 * 注文を表すエンティティ。
 *
 * @param id 識別子
 * @param orderDate 注文日時
 * @param customerName 購入者名
 * @param shippingAddress 出荷先の住所
 * @param shippingContact 出荷先の連絡先
 * @param orderItems [[com.github.j5ik2o.spetstore.domain.model.purchase.OrderItem]]のリスト
 */
case class Order(
  id: OrderId,
  status: StatusType.Value,
  orderStatus: OrderStatus.Value,
  orderDate: DateTime,
  customerId: CustomerId,
  customerName: String,
  shippingAddress: PostalAddress,
  shippingContact: Contact,
  orderItems: List[OrderItem],
  version: Option[Long]
)
    extends Entity[OrderId] {

  override def canEqual(other: Any) = other.isInstanceOf[Order]

  /**
   * [[com.github.j5ik2o.spetstore.domain.model.purchase.OrderItem]]の個数
   */
  val sizeOfOrderItems: Int = orderItems.size

  /**
   * [[com.github.j5ik2o.spetstore.domain.model.purchase.OrderItem]]の総数。
   */
  lazy val quantityOfOrderItems = orderItems.foldLeft(0)(_ + _.quantity)

  /**
   * 合計を取得する。
   *
   * @return 合計
   */
  def totalPrice(implicit ir: ItemRepository, ctx: EntityIOContext): Try[BigDecimal] = {
    orderItems.foldLeft(Try(BigDecimal(0))) {
      (l, r) =>
        for {
          e1 <- l
          e2 <- r.subTotalPrice
        } yield {
          e1 + e2
        }
    }
  }

  /**
   * この注文に[[com.github.j5ik2o.spetstore.domain.model.purchase.OrderItem]]を追加する。
   *
   * @param orderItem [[com.github.j5ik2o.spetstore.domain.model.purchase.OrderItem]]
   * @return 新しい[[com.github.j5ik2o.spetstore.domain.model.purchase.Order]]
   */
  def addOrderItem(orderItem: OrderItem): Order =
    copy(orderItems = orderItem :: orderItems)

  /**
   * この注文から[[com.github.j5ik2o.spetstore.domain.model.purchase.OrderItem]]を削除する。
   *
   * @param orderItem [[com.github.j5ik2o.spetstore.domain.model.purchase.OrderItem]]
   * @return 新しい[[com.github.j5ik2o.spetstore.domain.model.purchase.Order]]
   */
  def removeOrderItem(orderItem: OrderItem): Order =
    if (orderItems.exists(_ == orderItem)) {
      copy(orderItems = orderItems.filterNot(_ == orderItem))
    } else {
      this
    }

  /**
   * この注文から指定したインデックスの
   * [[com.github.j5ik2o.spetstore.domain.model.purchase.OrderItem]]を削除する。
   *
   * @param index インデックス
   * @return 新しい[[com.github.j5ik2o.spetstore.domain.model.purchase.Order]]
   */
  def removeOrderItemByIndex(index: Int): Order = {
    require(orderItems.size > index)
    val lb = ListBuffer(orderItems: _*)
    lb.remove(index)
    copy(orderItems = lb.result())
  }

  override def withVersion(version: Long): Entity[OrderId] = copy(version = Some(version))
}

/**
 * コンパニオンオブジェクト。
 */
object Order {

  /**
   * [[com.github.j5ik2o.spetstore.domain.model.purchase.Cart]]を清算する。
   *
   * [[com.github.j5ik2o.spetstore.domain.model.purchase.Cart]]から
   * [[com.github.j5ik2o.spetstore.domain.model.purchase.Order]]を
   * 生成する。
   *
   * @param cart [[com.github.j5ik2o.spetstore.domain.model.purchase.Cart]]
   * @return [[com.github.j5ik2o.spetstore.domain.model.purchase.Order]]
   */
  def clearUp(cart: Cart)(implicit is: IdentifierService, cr: CustomerRepository, ctx: EntityIOContext): Try[Order] = Try {
    val customer = cart.customer.get
    val orderItems = cart.cartItems.map(e => OrderItem.fromCartItem(OrderItemId(is.generate), e))
    Order(
      OrderId(is.generate),
      cart.status,
      OrderStatus.Pending,
      DateTime.now,
      customer.id,
      customer.name,
      customer.profile.postalAddress,
      customer.profile.contact,
      orderItems,
      None
    )
  }

}
