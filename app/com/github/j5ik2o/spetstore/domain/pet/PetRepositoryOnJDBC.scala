package com.github.j5ik2o.spetstore.domain.pet

import com.github.j5ik2o.spetstore.infrastructure.support.RepositoryOnJDBC
import scalikejdbc.WrappedResultSet
import java.util.UUID

private[pet]
class PetRepositoryOnJDBC
  extends RepositoryOnJDBC[PetId, Pet] with PetRepository {

  override def tableName: String = "pet"

  override def columnNames: Seq[String] = Seq(
    "id",
    "item_type_id",
    "name",
    "description",
    "price",
    "quantity"
  )

  protected def convertResultSetToEntity(resultSet: WrappedResultSet): Pet =
    Pet(
      id = PetId(UUID.fromString(resultSet.string("id"))),
      petTypeId = PetTypeId(UUID.fromString(resultSet.string("item_type_id"))),
      name = resultSet.string("name"),
      description = resultSet.stringOpt("description"),
      price = resultSet.long("price"),
      quantity = resultSet.int("quantity")
    )

  protected def convertEntityToValues(entity: Pet): Seq[Any] = Seq(
    entity.id,
    entity.petTypeId.value.toString,
    entity.name,
    entity.description,
    entity.price,
    entity.quantity
  )
}
