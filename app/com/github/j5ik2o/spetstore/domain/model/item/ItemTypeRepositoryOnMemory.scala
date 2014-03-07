package com.github.j5ik2o.spetstore.domain.model.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.RepositoryOnMemory
import scala.util.Try

/**
 * [[com.github.j5ik2o.spetstore.domain.model.item.ItemType]]のためのオンメモリリポジトリ。
 *
 * @param entities エンティティの集合
 */
private[item]
class ItemTypeRepositoryOnMemory(entities: Map[ItemTypeId, ItemType])
  extends RepositoryOnMemory[ItemTypeId, ItemType](entities) with ItemTypeRepository {

  protected def createInstance(entities: Map[ItemTypeId, ItemType]): This =
    new ItemTypeRepositoryOnMemory(entities)

}
