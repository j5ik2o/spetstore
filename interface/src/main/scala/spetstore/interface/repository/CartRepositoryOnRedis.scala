package spetstore.interface.repository

import akka.actor.ActorSystem
import cats.data.ReaderT
import com.github.j5ik2o.dddbase.redis.AggregateIOBaseFeature.RIO
import com.github.j5ik2o.dddbase.redis._
import com.github.j5ik2o.reactive.redis.RedisConnection
import monix.eval.Task
import org.sisioh.baseunits.scala.time.TimePoint
import spetstore.domain.model.UserAccountId
import spetstore.domain.model.basic.{ Quantity, StatusType }
import spetstore.domain.model.item.ItemId
import spetstore.domain.model.purchase._
import spetstore.interface.dao.CartComponent
import spetstore.interface.repository.CartRepositoryOnRedis.OnRedis
import spetstore.useCase.port.repository.CartRepository

import scala.concurrent.duration.Duration

object CartRepositoryOnRedis {
  type OnRedis[A] = ReaderT[Task, RedisConnection, A]
}

class CartRepositoryOnRedis(val expireDuration: Duration)(implicit system: ActorSystem)
    extends CartRepository[OnRedis]
    with AggregateSingleReadFeature
    with AggregateSingleWriteFeature
    with AggregateMultiReadFeature
    with AggregateMultiWriteFeature
    with AggregateSingleSoftDeleteFeature
    with AggregateMultiSoftDeleteFeature
    with CartComponent {

  override type RecordType = CartRecord
  override type DaoType    = CartDao
  override protected val dao = CartDao()

  override protected def convertToAggregate: CartRecord => RIO[Cart] = { record =>
    ReaderT { _ =>
      Task.pure(
        Cart(
          id = CartId(record.id),
          status = StatusType.Active,
          userAccountId = UserAccountId(record.userAccountId),
          cartItems = CartItems(record.cartItems.map { ci =>
            CartItem(
              CartItemId(ci.id),
              no = ci.no,
              itemId = ItemId(ci.itemId),
              quantity = Quantity(ci.quantity),
              inStock = ci.inStock
            )
          }),
          createdAt = TimePoint.from(record.createdAt.toInstant),
          updatedAt = record.updatedAt.map(TimePoint.from)
        )
      )
    }

  }

  override protected def convertToRecord: Cart => RIO[CartRecord] = { aggregate =>
    ReaderT { _ =>
      Task.pure(
        CartRecord(
          id = aggregate.id.value,
          status = aggregate.status.entryName,
          userAccountId = aggregate.userAccountId.value,
          cartItems = aggregate.cartItems.breachEncapsulationOfValues.map { ci =>
            CartItemRecord(
              id = ci.id.value,
              no = ci.no,
              itemId = ci.itemId.value,
              quantity = ci.quantity.breachEncapsulationOfValue,
              inStock = ci.inStock
            )
          },
          createdAt = aggregate.createdAt.asJavaZonedDateTime(),
          updatedAt = aggregate.updatedAt.map(_.asJavaZonedDateTime())
        )
      )
    }
  }

}
