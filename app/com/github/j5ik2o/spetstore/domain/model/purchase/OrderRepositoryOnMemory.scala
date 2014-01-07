package com.github.j5ik2o.spetstore.domain.model.purchase

import com.github.j5ik2o.spetstore.domain.infrastructure.support.RepositoryOnMemory

/**
 *
 * @param entities エンティティのマップ
 */
private[purchase]
class OrderRepositoryOnMemory(entities: Map[OrderId, Order])
  extends RepositoryOnMemory[OrderId, Order](entities) with OrderRepository {

  protected def createInstance(entities: Map[OrderId, Order]): This =
    new OrderRepositoryOnMemory(entities)

}
