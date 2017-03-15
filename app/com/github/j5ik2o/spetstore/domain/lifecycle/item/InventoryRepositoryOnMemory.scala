package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.support.support.RepositoryOnMemory
import com.github.j5ik2o.spetstore.domain.model.item.{ Inventory, InventoryId }

private[item] class InventoryRepositoryOnMemory(entities: Map[InventoryId, Inventory])
    extends RepositoryOnMemory[InventoryId, Inventory](entities) with InventoryRepository {

  protected def createInstance(entities: Map[InventoryId, Inventory]): This =
    new InventoryRepositoryOnMemory(entities)

}
