package com.j5ik2o.spetstore.domain.item

import com.j5ik2o.spetstore.infrastructure.support.RepositoryOnMemory

/**
 * [[com.j5ik2o.spetstore.domain.item.Item]]のためのオンメモリリポジトリ。
 *
 * @param entities エンティティの集合
 */
private[item]
class ItemRepositoryOnMemory(entities: Map[ItemId, Item])
  extends RepositoryOnMemory[ItemId, Item](entities) with ItemRepository {

  protected def createInstance(entities: Map[ItemId, Item]): This =
    new ItemRepositoryOnMemory(entities)

}
