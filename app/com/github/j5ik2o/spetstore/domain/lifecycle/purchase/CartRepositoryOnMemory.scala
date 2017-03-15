package com.github.j5ik2o.spetstore.domain.lifecycle.purchase

import com.github.j5ik2o.spetstore.domain.support.support.RepositoryOnMemory
import com.github.j5ik2o.spetstore.domain.model.purchase.{ Cart, CartId }

/**
 * [[purchase.CartRepository]]のためのオンメモリリポジトリ。
 *
 * @param entities エンティティの集合
 */
private[purchase] class CartRepositoryOnMemory(entities: Map[CartId, Cart])
    extends RepositoryOnMemory[CartId, Cart](entities) with CartRepository {

  protected def createInstance(entities: Map[CartId, Cart]): This =
    new CartRepositoryOnMemory(entities)

}
