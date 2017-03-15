package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.support.support.{ MultiIOSupport, Repository }
import com.github.j5ik2o.spetstore.domain.model.item.{ Item, ItemId }

/**
 * [[com.github.j5ik2o.spetstore.domain.model.item.Item]]のためのリポジトリ責務。
 */
trait ItemRepository extends Repository[ItemId, Item] with MultiIOSupport[ItemId, Item] {

  type This = ItemRepository

}

/**
 * コンパニオンオブジェクト。
 */
object ItemRepository {

  /**
   * メモリ用リポジトリを生成する。
   *
   * @param entities エンティティの集合
   * @return [[com.github.j5ik2o.spetstore.domain.lifecycle.item.ItemRepository]]
   */
  def ofMemory(entities: Map[ItemId, Item] = Map.empty): ItemRepository =
    new ItemRepositoryOnMemory(entities)

  /**
   * JDBC用リポジトリを生成する。
   *
   * @return [[com.github.j5ik2o.spetstore.domain.lifecycle.item.ItemRepository]]
   */
  def ofJDBC: ItemRepository =
    new ItemRepositoryOnJDBC

}
