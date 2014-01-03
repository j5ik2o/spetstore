package com.j5ik2o.spetstore.domain.item

import com.j5ik2o.spetstore.infrastructure.support.Repository

/**
 * [[com.j5ik2o.spetstore.domain.item.Item]]のためのリポジトリ責務。
 */
trait ItemRepository extends Repository[ItemId, Item]

/**
 * コンパニオンオブジェクト。
 */
object ItemRepository {

  /**
   * オンメモリリポジトリを生成する。
   *
   * @param entities エンティティの集合
   * @return [[com.j5ik2o.spetstore.domain.item.ItemRepository]]
   */
  def ofMemory(entities: Map[ItemId, Item] = Map.empty): ItemRepository =
    new ItemRepositoryOnMemory(entities)

}
