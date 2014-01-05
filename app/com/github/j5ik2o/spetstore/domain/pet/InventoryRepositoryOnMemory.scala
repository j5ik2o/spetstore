package com.github.j5ik2o.spetstore.domain.pet

import com.github.j5ik2o.spetstore.infrastructure.support.RepositoryOnMemory

private[pet]
class InventoryRepositoryOnMemory(entities: Map[InventoryId, Inventory])
  extends RepositoryOnMemory[InventoryId, Inventory](entities) with InventoryRepository {

  protected def createInstance(entities: Map[InventoryId, Inventory]): This =
    new InventoryRepositoryOnMemory(entities)

}
