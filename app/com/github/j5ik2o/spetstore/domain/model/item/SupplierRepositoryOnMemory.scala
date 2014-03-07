package com.github.j5ik2o.spetstore.domain.model.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.RepositoryOnMemory
import scala.util.Try

private[item]
class SupplierRepositoryOnMemory(entities: Map[SupplierId, Supplier])
  extends RepositoryOnMemory[SupplierId, Supplier](entities) with SupplierRepository {

  protected def createInstance(entities: Map[SupplierId, Supplier]): This =
    new SupplierRepositoryOnMemory(entities)

}
