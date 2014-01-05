package com.j5ik2o.spetstore.domain.item

import com.j5ik2o.spetstore.infrastructure.support.RepositoryOnJDBC
import scalikejdbc.WrappedResultSet
import java.util.UUID

private[item]
class CategoryRepositoryOnJDBC
extends RepositoryOnJDBC[CategoryId, Category] with CategoryRepository {


  override val tableName = "category"

  override val columnNames = Seq(
    "id",
    "name",
    "description"
  )

  protected def convertToEntity(resultSet: WrappedResultSet): Category =
    Category(
      id = CategoryId(UUID.fromString(resultSet.string("id"))),
      name = resultSet.string("name"),
      description = resultSet.stringOpt("description")
    )

  protected def convertToValues(entity: Category): Seq[Any] = Seq(
    entity.id,
    entity.name,
    entity.description
  )

}
