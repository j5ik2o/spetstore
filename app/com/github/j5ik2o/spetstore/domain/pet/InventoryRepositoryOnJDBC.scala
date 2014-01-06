package com.github.j5ik2o.spetstore.domain.pet

import com.github.j5ik2o.spetstore.infrastructure.support.RepositoryOnJDBC
import scalikejdbc.WrappedResultSet
import java.util.UUID

private[pet]
class InventoryRepositoryOnJDBC
  extends RepositoryOnJDBC[InventoryId, Inventory] with InventoryRepository {

  override def tableName: String = "inventory"

  override def columnNames: Seq[String] = Seq(
    "id",
    "pet_id",
    "quantity"
  )

  protected def convertResultSetToEntity(resultSet: WrappedResultSet): Inventory =
    Inventory(
      id = InventoryId(UUID.fromString(resultSet.string("id"))),
      petId = PetId(UUID.fromString(resultSet.string("pet_id"))),
      quantity = resultSet.int("quantity")
    )

  protected def convertEntityToValues(entity: Inventory): Seq[Any] = Seq(
    entity.id.value.toString,
    entity.petId.value.toString,
    entity.quantity
  )

}
