package com.github.j5ik2o.spetstore.domain.lifecycle.purchase

import com.github.j5ik2o.spetstore.domain.support.support.RepositoryOnMemory
import com.github.j5ik2o.spetstore.domain.model.purchase.{ Order, OrderId }

/**
 *
 * @param entities エンティティのマップ
 */
private[purchase] class OrderRepositoryOnMemory(entities: Map[OrderId, Order])
    extends RepositoryOnMemory[OrderId, Order](entities) with OrderRepository {

  protected def createInstance(entities: Map[OrderId, Order]): This =
    new OrderRepositoryOnMemory(entities)

}
