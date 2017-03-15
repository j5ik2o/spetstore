package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.support.support.RepositoryOnMemory
import com.github.j5ik2o.spetstore.domain.model.item.{ Item, ItemId }

/**
 * [[com.github.j5ik2o.spetstore.domain.model.item.Item]]のためのオンメモリリポジトリ。
 *
 * @param entities エンティティの集合
 */
private[item] class ItemRepositoryOnMemory(entities: Map[ItemId, Item])
    extends RepositoryOnMemory[ItemId, Item](entities) with ItemRepository {

  protected def createInstance(entities: Map[ItemId, Item]): This =
    new ItemRepositoryOnMemory(entities)

}
