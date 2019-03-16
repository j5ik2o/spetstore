package spetstore.interface.repository

import com.github.j5ik2o.dddbase.slick._
import io.circe.parser._
import io.circe.syntax._
import monix.eval.Task
import org.sisioh.baseunits.scala.money.Money
import org.sisioh.baseunits.scala.time.TimePoint
import slick.jdbc.JdbcProfile
import spetstore.domain.model.basic.{ Price, StatusType }
import spetstore.domain.model.item._
import spetstore.interface.dao.ItemComponent

class ItemRepositoryBySlick(override val profile: JdbcProfile, override val db: JdbcProfile#Backend#Database)
    extends AbstractItemRepositoryBySlick(profile, db)
    with AggregateSingleSoftDeleteFeature
    with AggregateMultiSoftDeleteFeature

abstract class AbstractItemRepositoryBySlick(val profile: JdbcProfile, val db: JdbcProfile#Backend#Database)
    extends ItemRepository[Task]
    with AggregateSingleReadFeature
    with AggregateAllReadFeature
    with AggregateMultiReadFeature
    with AggregateSingleWriteFeature
    with AggregateMultiWriteFeature
    with ItemComponent {

  override type RecordType = ItemRecord
  override type TableType  = Items
  protected override val dao = ItemDao

  import profile.api._

  override protected def byCondition(id: IdType): TableType => Rep[Boolean] = {
    _.id === id.value
  }

  override protected def byConditions(ids: Seq[IdType]): TableType => Rep[Boolean] = {
    _.id.inSet(ids.map(_.value))
  }

  override protected def convertToAggregate: ItemRecord => Task[Item] = { record =>
    for {
      categoryValues <- Task.fromTry(
        parse(record.categories).flatMap(_.as[Set[String]]).toTry
      )
      result <- Task.now(
        Item(
          id = ItemId(record.id),
          status = StatusType.withName(record.status),
          name = ItemName(record.name),
          description = record.description.map(ItemDescription),
          categories = Categories(categoryValues),
          price = Price(Money.yens(BigDecimal(record.price))),
          createdAt = TimePoint.from(record.createdAt.toInstant),
          updatedAt = record.updatedAt.map(v => TimePoint.from(v))
        )
      )
    } yield result
  }

  override protected def convertToRecord: Item => Task[ItemRecord] = { aggregate =>
    Task.now(
      ItemRecord(
        id = aggregate.id.value,
        status = aggregate.status.entryName,
        name = aggregate.name.breachEncapsulationOfValue,
        description = aggregate.description.map(_.breachEncapsulationOfValue),
        categories = aggregate.categories.breachEncapsulationOfValues.asJson.noSpaces,
        price = aggregate.price.breachEncapsulationOfValue.amount.toLong,
        createdAt = aggregate.createdAt.asJavaZonedDateTime(),
        updatedAt = aggregate.updatedAt.map(_.asJavaZonedDateTime())
      )
    )
  }

}
