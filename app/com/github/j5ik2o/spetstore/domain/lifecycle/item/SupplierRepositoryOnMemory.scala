package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.support.support.RepositoryOnMemory
import com.github.j5ik2o.spetstore.domain.model.item.{ Supplier, SupplierId }

private[item] class SupplierRepositoryOnMemory(entities: Map[SupplierId, Supplier])
    extends RepositoryOnMemory[SupplierId, Supplier](entities) with SupplierRepository {

  protected def createInstance(entities: Map[SupplierId, Supplier]): This =
    new SupplierRepositoryOnMemory(entities)

}
