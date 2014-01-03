package com.j5ik2o.spetstore.domain.item

import com.j5ik2o.spetstore.infrastructure.support.Repository

/**
 * [[com.j5ik2o.spetstore.domain.item.ItemType]]のためのリポジトリ責務。
 */
trait ItemTypeRepository extends Repository[ItemTypeId, ItemType]

/**
 * コンパニオンオブジェクト。
 */
object ItemTypeRepository {

  /**
   * オンメモリリポジトリを生成する。
   *
   * @param entities エンティティの集合
   * @return [[com.j5ik2o.spetstore.domain.item.ItemTypeRepository]]
   */
  def ofMemory(entities: Map[ItemTypeId, ItemType] = Map.empty): ItemTypeRepository =
    new ItemTypeRepositoryOnMemory(entities)

}

