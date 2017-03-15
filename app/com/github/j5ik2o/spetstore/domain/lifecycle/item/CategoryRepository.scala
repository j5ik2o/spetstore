package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.support.support.{ MultiIOSupport, Repository }
import com.github.j5ik2o.spetstore.domain.model.item.{ Category, CategoryId }

/**
 * [[com.github.j5ik2o.spetstore.domain.model.item.Category]]のためのリポジトリ責務。
 */
trait CategoryRepository extends Repository[CategoryId, Category] with MultiIOSupport[CategoryId, Category] {

  type This = CategoryRepository

}

/**
 * コンパニオンオブジェクト。
 */
object CategoryRepository {

  /**
   * メモリ用リポジトリを生成する。
   *
   * @param entities エンティティの集合
   * @return [[com.github.j5ik2o.spetstore.domain.lifecycle.item.CategoryRepository]]
   */
  def ofMemory(entities: Map[CategoryId, Category] = Map.empty): CategoryRepository =
    new CategoryRepositoryOnMemory(entities)

  /**
   * JDBC用リポジトリを生成する。
   *
   * @return [[com.github.j5ik2o.spetstore.domain.lifecycle.item.CategoryRepository]]
   */
  def ofJDBC: CategoryRepository =
    new CategoryRepositoryOnJDBC

}
