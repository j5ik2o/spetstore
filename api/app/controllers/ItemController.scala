package controllers

import controllers.models.ItemJson
import io.circe.generic.auto._
import io.circe.syntax._
import javax.inject._
import monix.execution.Scheduler
import play.api.libs.circe._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import spetstore.domain.model.item.{Item, ItemId}
import spetstore.interface.repository.ItemRepositoryTask

import scala.concurrent.ExecutionContext

@Singleton
class ItemController @Inject()(cc: ControllerComponents, repository: ItemRepositoryTask)(implicit ec: ExecutionContext)
  extends AbstractController(cc)
    with Circe {

  private implicit val scheduler = Scheduler(ec)

  def get(id: Long): Action[AnyContent] = Action.async {
    repository.resolveById(ItemId(id)).runAsync.map { m =>
      val json = ItemJson(
        m.id.value,
        m.name.breachEncapsulationOfValue,
        m.description.map(_.breachEncapsulationOfValue),
        m.categories.breachEncapsulationOfValues,
        m.price.breachEncapsulationOfValue.amount.toLong,
        m.createdAt.toInstant.toEpochMilli,
        m.updatedAt.map(_.toInstant.toEpochMilli)
      ).asJson
      Ok(json)
    }
  }

  def list(): Action[AnyContent] = Action.async {
    repository.resolveAll.runAsync.map { ms =>
      Ok(ms.map { m =>
        ItemJson(
          m.id.value,
          m.name.breachEncapsulationOfValue,
          m.description.map(_.breachEncapsulationOfValue),
          m.categories.breachEncapsulationOfValues,
          m.price.breachEncapsulationOfValue.amount.toLong,
          m.createdAt.toInstant.toEpochMilli,
          m.updatedAt.map(_.toInstant.toEpochMilli)
        ).asJson
      })
    }
  }

}
