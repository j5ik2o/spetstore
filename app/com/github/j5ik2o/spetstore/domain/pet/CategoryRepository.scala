package com.github.j5ik2o.spetstore.domain.pet

import com.github.j5ik2o.spetstore.infrastructure.support.Repository

/**
 * [[com.github.j5ik2o.spetstore.domain.pet.Category]]のためのリポジトリ責務。
 */
trait CategoryRepository extends Repository[CategoryId, Category] {

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
   * @return [[com.github.j5ik2o.spetstore.domain.pet.CategoryRepository]]
   */
  def ofMemory(entities: Map[CategoryId, Category] = Map.empty): CategoryRepository =
    new CategoryRepositoryOnMemory(entities)

  /**
   * JDBC用リポジトリを生成する。
   *
   * @return [[com.github.j5ik2o.spetstore.domain.pet.CategoryRepository]]
   */
  def ofJDBC: CategoryRepository =
    new CategoryRepositoryOnJDBC

}
