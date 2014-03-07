package com.github.j5ik2o.spetstore.domain.model.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.RepositoryOnJDBC
import java.util.UUID
import scalikejdbc._, SQLInterpolation._


private[item]
class CategoryRepositoryOnJDBC
  extends RepositoryOnJDBC[CategoryId, Category] with CategoryRepository {

  override def defaultAlias = createAlias("c")
  override val tableName = "category"

  def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[Category]): Category =
    Category(
      id = CategoryId(UUID.fromString(rs.get(n.id))),
      name = rs.get(n.name),
      description = rs.get(n.description)
    )


  protected def toNamedValues(entity: Category): Seq[(Symbol, Any)] = Seq(
    'id -> entity.id.value,
    'name -> entity.name,
    'description -> entity.description
  )

}
