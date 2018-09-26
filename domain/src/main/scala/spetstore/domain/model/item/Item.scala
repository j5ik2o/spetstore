package spetstore.domain.model.item

import java.time.ZonedDateTime

import com.github.j5ik2o.dddbase.{ Aggregate, AggregateLongId }
import spetstore.domain.model.basic.StatusType

import scala.reflect._

case class ItemId(value: Long) extends AggregateLongId

case class ItemName(breachEncapsulationOfValue: String) {
  require(breachEncapsulationOfValue.length < 255)
}

case class ItemDescription(breachEncapsulationOfValue: String) {
  require(breachEncapsulationOfValue.length < 255)
}

case class Item(id: ItemId,
                status: StatusType,
                name: ItemName,
                description: Option[ItemDescription],
                categories: Categories,
                price: Price,
                createdAt: ZonedDateTime,
                updatedAt: Option[ZonedDateTime])
    extends Aggregate {
  override type IdType        = ItemId
  override type AggregateType = Item
  override protected val tag: ClassTag[Item] = classTag[Item]
}
