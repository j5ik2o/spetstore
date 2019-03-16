package spetstore.domain.model.item

import com.github.j5ik2o.dddbase.Aggregate
import org.sisioh.baseunits.scala.time.TimePoint
import spetstore.domain.model.basic.{ Price, StatusType }

import scala.reflect._

case class Item(id: ItemId,
                status: StatusType,
                name: ItemName,
                description: Option[ItemDescription],
                categories: Categories,
                price: Price,
                createdAt: TimePoint,
                updatedAt: TimePoint)
    extends Aggregate {
  override type IdType        = ItemId
  override type AggregateType = Item
  override protected val tag: ClassTag[Item] = classTag[Item]
}
