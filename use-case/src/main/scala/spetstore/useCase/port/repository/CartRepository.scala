package spetstore.useCase.port.repository

import cats.data.ReaderT
import com.github.j5ik2o.dddbase._
import com.github.j5ik2o.reactive.redis.RedisConnection
import monix.eval.Task
import spetstore.domain.model.purchase.{ Cart, CartId }

object CartRepository {

  type OnRedis[A] = ReaderT[Task, RedisConnection, A]

}

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
