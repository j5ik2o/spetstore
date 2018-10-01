package spetstore.domain.model.purchase

import cats.implicits._
import spetstore.domain.model.basic.Price
import spetstore.domain.model.item.ItemId

import scala.collection.mutable.ListBuffer

case class OrderItems(breachEncapsulationOfValues: Seq[OrderItem]) {

  val size: Int = breachEncapsulationOfValues.size

  def totalPrice(itemPriceResolver: ItemId => Price): Price =
    breachEncapsulationOfValues.foldLeft(Price.Zero) { (result, item) =>
      result combine item.subTotal(itemPriceResolver)
    }

  def addOrderItem(orderItem: OrderItem): OrderItems =
    copy(breachEncapsulationOfValues = orderItem +: breachEncapsulationOfValues)

  def removeOrderItem(orderItem: OrderItem): OrderItems =
    if (breachEncapsulationOfValues.contains(orderItem)) {
      copy(breachEncapsulationOfValues = breachEncapsulationOfValues.filterNot(_ == orderItem))
    } else {
      this
    }

  def removeOrderItemByIndex(index: Int): OrderItems = {
    require(breachEncapsulationOfValues.size > index)
    val lb = ListBuffer(breachEncapsulationOfValues: _*)
    lb.remove(index)
    copy(breachEncapsulationOfValues = lb.result())
  }

}
