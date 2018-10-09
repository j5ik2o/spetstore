package spetstore.useCase.port.repository

import com.github.j5ik2o.dddbase._
import spetstore.domain.model.item.{ Item, ItemId }

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
