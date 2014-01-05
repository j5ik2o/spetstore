package com.github.j5ik2o.spetstore.domain.pet

import com.github.j5ik2o.spetstore.infrastructure.support.RepositoryOnJDBC
import scalikejdbc.WrappedResultSet
import java.util.UUID

private[pet]
class PetTypeRepositoryOnJDBC
extends RepositoryOnJDBC[PetTypeId, PetType] with PetTypeRepository {

  override def tableName: String = "item_type"

  override def columnNames: Seq[String] = Seq(
    "id",
    "category_id",
    "name",
    "description"
  )

  protected def convertResultSetToEntity(resultSet: WrappedResultSet): PetType =
    PetType(
      id = PetTypeId(UUID.fromString(resultSet.string("id"))),
      categoryId = CategoryId(UUID.fromString("category_id")),
      name = resultSet.string("name"),
      description = resultSet.stringOpt("description")
    )

  protected def convertEntityToValues(entity: PetType): Seq[Any] = Seq(
    entity.id,
    entity.categoryId.value.toString,
    entity.name,
    entity.description
  )

}
