package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.RepositoryOnJDBC
import com.github.j5ik2o.spetstore.domain.model.item.{Category, CategoryId}
import java.util.UUID
import scalikejdbc._, SQLInterpolation._


private[item]
class CategoryRepositoryOnJDBC
  extends RepositoryOnJDBC[CategoryId, Category] with CategoryRepository {

  class Dao extends AbstractDao[Category] {
    override def defaultAlias = createAlias("c")

    override val tableName = "category"

    def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[Category]): Category =
      Category(
        id = CategoryId(UUID.fromString(rs.get(n.id))),
        name = rs.get(n.name),
        description = rs.get(n.description)
      )


    def toNamedValues(entity: Category): Seq[(Symbol, Any)] = Seq(
      'id -> entity.id.value,
      'name -> entity.name,
      'description -> entity.description
    )
  }

  override protected def createDao = new Dao
}
