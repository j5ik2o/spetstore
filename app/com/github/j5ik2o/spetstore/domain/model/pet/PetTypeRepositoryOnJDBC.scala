package com.github.j5ik2o.spetstore.domain.model.pet

import com.github.j5ik2o.spetstore.domain.infrastructure.support.RepositoryOnJDBC
import scalikejdbc.{SQLInterpolation, WrappedResultSet}
import java.util.UUID

import scalikejdbc._, SQLInterpolation._


private[pet]
class PetTypeRepositoryOnJDBC
extends RepositoryOnJDBC[PetTypeId, PetType] with PetTypeRepository {

  override def defaultAlias = createAlias("i")

  override def tableName: String = "item_type"

  def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[PetType]): PetType =
    PetType(
      id = PetTypeId(UUID.fromString(rs.get(n.id))),
      categoryId = CategoryId(UUID.fromString(rs.get(n.categoryId))),
      name = rs.get(n.name),
      description = rs.get(n.description)
    )

  protected def toNamedValues(entity: PetType): Seq[(Symbol, Any)] = Seq(
    'id -> entity.id.value,
    'categoryId -> entity.categoryId.value,
    'name -> entity.name,
    'description -> entity.description
  )

}
