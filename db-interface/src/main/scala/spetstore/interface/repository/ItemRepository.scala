package spetstore.interface.repository

import com.github.j5ik2o.dddbase._
import monix.eval.Task
import spetstore.domain.model.item.{ Item, ItemId }

trait ItemRepositoryTask extends ItemRepository[Task]

trait ItemRepository[M[_]]
    extends AggregateSingleReader[M]
    with AggregateMultiReader[M]
    with AggregateAllReader[M]
    with AggregateSingleWriter[M]
    with AggregateMultiWriter[M]
    with AggregateSingleSoftDeletable[M]
    with AggregateMultiSoftDeletable[M] {
  override type IdType        = ItemId
  override type AggregateType = Item
}
