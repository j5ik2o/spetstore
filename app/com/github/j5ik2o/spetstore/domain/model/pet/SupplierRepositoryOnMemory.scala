package com.github.j5ik2o.spetstore.domain.model.pet

import com.github.j5ik2o.spetstore.domain.infrastructure.support.RepositoryOnMemory

private[pet]
class SupplierRepositoryOnMemory(entities: Map[SupplierId, Supplier])
  extends RepositoryOnMemory[SupplierId, Supplier](entities) with SupplierRepository {

  protected def createInstance(entities: Map[SupplierId, Supplier]): This =
    new SupplierRepositoryOnMemory(entities)

}
