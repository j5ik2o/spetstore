package spetstore.interface.repository

import com.github.j5ik2o.dddbase.slick.AggregateIOBaseFeature.RIO
import com.github.j5ik2o.dddbase.slick._
import io.circe.parser._
import io.circe.syntax._
import javax.inject._
import monix.eval.Task
import org.sisioh.baseunits.scala.money.Money
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import spetstore.domain.model.basic.StatusType
import spetstore.domain.model.item._
import spetstore.interface.dao.ItemComponent

@Singleton
class ItemRepositoryBySlick @Inject()(dbConfigProvider: DatabaseConfigProvider)
    extends ItemRepositoryTask
    with AggregateSingleReadFeature
    with AggregateAllReadFeature
    with AggregateMultiReadFeature
    with AggregateSingleWriteFeature
    with AggregateMultiWriteFeature
    with AggregateSingleSoftDeleteFeature
    with AggregateMultiSoftDeleteFeature
    with ItemComponent {

  private val dbConfig                 = dbConfigProvider.get[JdbcProfile]
  val profile: JdbcProfile             = dbConfig.profile
  val db: JdbcProfile#Backend#Database = dbConfig.db

  override type RecordType = ItemRecord
  override type TableType  = Items
  protected override val dao = ItemDao

  override protected def convertToAggregate: ItemRecord => RIO[Item] = { record =>
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
          createdAt = record.createdAt,
          updatedAt = record.updatedAt
        )
      )
    } yield result
  }

  override protected def convertToRecord: Item => RIO[ItemRecord] = { aggregate =>
    Task.now(
      ItemRecord(
        id = aggregate.id.value,
        status = aggregate.status.entryName,
        name = aggregate.name.breachEncapsulationOfValue,
        description = aggregate.description.map(_.breachEncapsulationOfValue),
        categories = aggregate.categories.breachEncapsulationOfValues.asJson.noSpaces,
        price = aggregate.price.breachEncapsulationOfValue.amount.toLong,
        createdAt = aggregate.createdAt,
        updatedAt = aggregate.updatedAt
      )
    )
  }
}
