package com.github.j5ik2o.spetstore.domain.pet

import com.github.j5ik2o.spetstore.infrastructure.support.RepositoryOnJDBC
import scalikejdbc.WrappedResultSet
import java.util.UUID

private[pet]
class CategoryRepositoryOnJDBC
extends RepositoryOnJDBC[CategoryId, Category] with CategoryRepository {


  override val tableName = "category"

  override val columnNames = Seq(
    "id",
    "name",
    "description"
  )

  protected def convertResultSetToEntity(resultSet: WrappedResultSet): Category =
    Category(
      id = CategoryId(UUID.fromString(resultSet.string("id"))),
      name = resultSet.string("name"),
      description = resultSet.stringOpt("description")
    )

  protected def convertEntityToValues(entity: Category): Seq[Any] = Seq(
    entity.id,
    entity.name,
    entity.description
  )

}
