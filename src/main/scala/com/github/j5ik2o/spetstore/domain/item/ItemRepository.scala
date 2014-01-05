package com.github.j5ik2o.spetstore.domain.item

import com.github.j5ik2o.spetstore.infrastructure.support.Repository

/**
 * [[com.github.j5ik2o.spetstore.domain.item.Item]]のためのリポジトリ責務。
 */
trait ItemRepository extends Repository[ItemId, Item] {

  type This = ItemRepository

}

/**
 * コンパニオンオブジェクト。
 */
object ItemRepository {

  /**
   * オンメモリリポジトリを生成する。
   *
   * @param entities エンティティの集合
   * @return [[com.github.j5ik2o.spetstore.domain.item.ItemRepository]]
   */
  def ofMemory(entities: Map[ItemId, Item] = Map.empty): ItemRepository =
    new ItemRepositoryOnMemory(entities)

  /**
   * JDBC用リポジトリを生成する。
   *
   * @return [[com.github.j5ik2o.spetstore.domain.item.ItemRepository]]
   */
  def ofJDBC: ItemRepository =
    new ItemRepositoryOnJDBC

}
