package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.support.support.RepositoryOnMemory
import com.github.j5ik2o.spetstore.domain.model.item.{ ItemType, ItemTypeId }

/**
 * [[com.github.j5ik2o.spetstore.domain.model.item.ItemType]]のためのオンメモリリポジトリ。
 *
 * @param entities エンティティの集合
 */
private[item] class ItemTypeRepositoryOnMemory(entities: Map[ItemTypeId, ItemType])
    extends RepositoryOnMemory[ItemTypeId, ItemType](entities) with ItemTypeRepository {

  protected def createInstance(entities: Map[ItemTypeId, ItemType]): This =
    new ItemTypeRepositoryOnMemory(entities)

}
