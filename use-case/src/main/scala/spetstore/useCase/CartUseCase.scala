package spetstore.useCase

import java.util.UUID

import akka.NotUsed
import akka.stream.scaladsl.{ Flow, Source }
import com.github.j5ik2o.reactive.redis.RedisConnection
import monix.eval.Task
import monix.execution.Scheduler
import org.sisioh.baseunits.scala.timeutil.Clock
import spetstore.domain.model.basic.StatusType
import spetstore.domain.model.purchase.{ Cart, CartId, CartItems }
import spetstore.useCase.model.{ CreateCartRequest, CreateCartResponse }
import spetstore.useCase.port.repository.CartRepository
import spetstore.useCase.port.repository.CartRepository.OnRedis
import wvlet.airframe.bind

trait CartUseCase {

  private val cartRepository = bind[CartRepository[OnRedis]]

  def create(implicit scheduler: Scheduler,
             conn: RedisConnection): Flow[CreateCartRequest, CreateCartResponse, NotUsed] =
    Flow[CreateCartRequest].mapAsync(1) { cart =>
      (for {
        id <- Task.pure(CartId(UUID.randomUUID().toString))
        _ <- cartRepository
          .store(
            Cart(
              id = CartId(UUID.randomUUID().toString),
              status = StatusType.Active,
              userAccountId = cart.userAccountId,
              cartItems = CartItems.ZERO,
              createdAt = Clock.now,
              updatedAt = None
            )
          ).run(conn)
      } yield CreateCartResponse(id.value)).runAsync
    }

  def resolveById(id: CartId)(implicit scheduler: Scheduler, conn: RedisConnection): Source[Cart, NotUsed] =
    Source.fromFuture {
      cartRepository.resolveById(id).run(conn).runAsync
    }
}
