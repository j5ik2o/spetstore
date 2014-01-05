package com.github.j5ik2o.spetstore.domain.item

import com.github.j5ik2o.spetstore.infrastructure.support.Repository

/**
 * [[com.github.j5ik2o.spetstore.domain.item.ItemType]]のためのリポジトリ責務。
 */
trait ItemTypeRepository extends Repository[ItemTypeId, ItemType] {

  type This = ItemTypeRepository

}

/**
 * コンパニオンオブジェクト。
 */
object ItemTypeRepository {

  /**
   * メモリ用リポジトリを生成する。
   *
   * @param entities エンティティのマップ
   * @return [[com.github.j5ik2o.spetstore.domain.item.ItemTypeRepository]]
   */
  def ofMemory(entities: Map[ItemTypeId, ItemType] = Map.empty): ItemTypeRepository =
    new ItemTypeRepositoryOnMemory(entities)

  /**
   * JDBC用リポジトリを生成する。
   *
   * @return [[com.github.j5ik2o.spetstore.domain.item.ItemTypeRepository]]
   */
  def ofJDBC: ItemTypeRepository =
    new ItemTypeRepositoryOnJDBC

}

