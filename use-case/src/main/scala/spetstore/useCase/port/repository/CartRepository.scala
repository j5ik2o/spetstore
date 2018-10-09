package spetstore.useCase.port.repository

import com.github.j5ik2o.dddbase._
import spetstore.domain.model.purchase.{ Cart, CartId }

trait CartRepository[M[_]]
    extends AggregateSingleReader[M]
    with AggregateMultiReader[M]
    with AggregateSingleWriter[M]
    with AggregateMultiWriter[M]
    with AggregateSingleSoftDeletable[M]
    with AggregateMultiSoftDeletable[M] {
  override type IdType        = CartId
  override type AggregateType = Cart
}
