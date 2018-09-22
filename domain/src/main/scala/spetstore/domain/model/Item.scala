package spetstore.domain.model

import _root_.io.circe
import com.github.j5ik2o.dddbase.{ Aggregate, AggregateLongId }
import io.circe.parser
import io.circe.syntax._
import spetstore.domain.model.basic.StatusType

import scala.reflect._

case class ItemId(value: Long) extends AggregateLongId

case class Categories(breachEncapsulationOfValues: Set[String]) {
  def asString: String = breachEncapsulationOfValues.asJson.noSpaces
}

object Categories {
  def parse(text: String): Either[circe.Error, Categories] = {
    parser.parse(text).flatMap(_.as[Set[String]]).map(Categories.apply)
  }
}

case class ItemName(breachEncapsulationOfValue: String) {
  require(breachEncapsulationOfValue.length < 255)
}

case class ItemDescription(breachEncapsulationOfValue: String) {
  require(breachEncapsulationOfValue.length < 255)
}

case class Price(breachEncapsulationOfValue: BigDecimal) {}

case class Item(id: ItemId,
                status: StatusType,
                name: ItemName,
                description: Option[ItemDescription],
                categories: Categories,
                price: Price)
    extends Aggregate {
  override type IdType        = ItemId
  override type AggregateType = Item
  override protected val tag: ClassTag[Item] = classTag[Item]
}
