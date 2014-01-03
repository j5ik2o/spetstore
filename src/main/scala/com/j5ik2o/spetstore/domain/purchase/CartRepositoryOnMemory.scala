package com.j5ik2o.spetstore.domain.purchase

import com.j5ik2o.spetstore.infrastructure.support.RepositoryOnMemory

/**
 * [[com.j5ik2o.spetstore.domain.purchase.CartRepository]]のためのオンメモリリポジトリ。
 *
 * @param entities エンティティの集合
 */
private[purchase]
class CartRepositoryOnMemory(entities: Map[CartId, Cart])
extends RepositoryOnMemory[CartId, Cart](entities) with CartRepository {

  type This = CartRepository

  protected def createInstance(entities: Map[CartId, Cart]): This =
    new CartRepositoryOnMemory(entities)

}
