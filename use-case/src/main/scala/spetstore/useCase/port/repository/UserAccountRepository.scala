package spetstore.useCase.port.repository

import spetstore.domain.model.{ UserAccount, UserAccountId }
import com.github.j5ik2o.dddbase._

trait UserAccountRepository[M[_]]
    extends AggregateSingleReader[M]
    with AggregateSingleWriter[M]
    with AggregateMultiReader[M]
    with AggregateMultiWriter[M]
    with AggregateSingleSoftDeletable[M]
    with AggregateMultiSoftDeletable[M] {
  override type IdType        = UserAccountId
  override type AggregateType = UserAccount
}
