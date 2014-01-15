package com.github.j5ik2o.spetstore.domain.model.pet

import com.github.j5ik2o.spetstore.domain.infrastructure.support.RepositoryOnJDBC
import java.util.UUID
import scalikejdbc._, SQLInterpolation._

private[pet]
class InventoryRepositoryOnJDBC
  extends RepositoryOnJDBC[InventoryId, Inventory] with InventoryRepository {

  override def defaultAlias = createAlias("i")

  override val connectionPoolName = 'inventory

  override val tableName = "inventory"

  def extract(rs: WrappedResultSet, p: SQLInterpolation.ResultName[Inventory]): Inventory =
    Inventory(
      id = InventoryId(UUID.fromString(rs.get(p.id))),
      petId = PetId(UUID.fromString(rs.get(p.petId))),
      quantity = rs.get(p.quantity)
    )

  protected def toNamedValues(entity: Inventory): Seq[(Symbol, Any)] = Seq(
    'id -> entity.id.value,
    'petId -> entity.petId.value,
    'quantity -> entity.quantity
  )

}
