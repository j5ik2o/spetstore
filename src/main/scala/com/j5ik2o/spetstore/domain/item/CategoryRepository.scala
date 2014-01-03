package com.j5ik2o.spetstore.domain.item

import com.j5ik2o.spetstore.infrastructure.support.Repository

/**
 * [[com.j5ik2o.spetstore.domain.item.Category]]のためのリポジトリ責務。
 */
trait CategoryRepository extends Repository[CategoryId, Category]

/**
 * コンパニオンオブジェクト。
 */
object CategoryRepository {

  /**
   * オンメモリリポジトリを生成する。
   *
   * @param entities エンティティの集合
   * @return [[com.j5ik2o.spetstore.domain.item.CategoryRepository]]
   */
  def ofMemory(entities: Map[CategoryId, Category]): CategoryRepository =
    new CategoryRepositoryOnMemory(entities)

}
