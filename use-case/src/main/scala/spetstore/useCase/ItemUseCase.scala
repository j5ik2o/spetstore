package spetstore.useCase

import akka.NotUsed
import akka.stream.scaladsl.{ Flow, Source }
import monix.eval.Task
import monix.execution.Scheduler
import org.sisioh.baseunits.scala.money.Money
import org.sisioh.baseunits.scala.timeutil.Clock
import spetstore.domain.model.basic.{ Price, StatusType }
import spetstore.domain.model.item._
import spetstore.useCase.model.{ CreateItemRequest, CreateItemResponse }
import spetstore.useCase.port.generator.IdGenerator
import spetstore.useCase.port.repository.ItemRepository
import wvlet.airframe.bind

trait ItemUseCase {

  private val itemIdGenerator = bind[IdGenerator[ItemId]]
  private val itemRepository  = bind[ItemRepository[Task]]

  def create(implicit scheduler: Scheduler): Flow[CreateItemRequest, CreateItemResponse, NotUsed] =
    Flow[CreateItemRequest].mapAsync(1) { item =>
      val now = Clock.now
      (for {
        id <- itemIdGenerator.generateId()
        _ <- itemRepository.store(
          Item(
            id,
            StatusType.Active,
            ItemName(item.name),
            item.description.map(ItemDescription),
            Categories(item.categories),
            Price(Money.yens(item.price)),
            now,
            now
          )
        )
      } yield CreateItemResponse(id)).runToFuture
    }

  def resolveById(id: ItemId)(implicit scheduler: Scheduler): Source[Item, NotUsed] =
    Source.fromFuture {
      itemRepository.resolveById(id).runToFuture
    }
}
