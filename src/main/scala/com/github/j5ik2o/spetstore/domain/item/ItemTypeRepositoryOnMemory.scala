package com.github.j5ik2o.spetstore.domain.item

import com.github.j5ik2o.spetstore.infrastructure.support.RepositoryOnMemory

/**
 * [[com.github.j5ik2o.spetstore.domain.item.ItemType]]のためのオンメモリリポジトリ。
 *
 * @param entities エンティティの集合
 */
private[item]
class ItemTypeRepositoryOnMemory(entities: Map[ItemTypeId, ItemType])
  extends RepositoryOnMemory[ItemTypeId, ItemType](entities) with ItemTypeRepository {

  protected def createInstance(entities: Map[ItemTypeId, ItemType]): This =
    new ItemTypeRepositoryOnMemory(entities)

}
