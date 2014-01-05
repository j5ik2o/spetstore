package com.j5ik2o.spetstore.domain.item

import com.j5ik2o.spetstore.infrastructure.support.RepositoryOnMemory

/**
 * [[com.j5ik2o.spetstore.domain.item.CategoryRepository]]のためのオンメモリリポジトリ。
 *
 * @param entities エンティティの集合
 */
private[item]
class CategoryRepositoryOnMemory(entities: Map[CategoryId, Category])
  extends RepositoryOnMemory[CategoryId, Category](entities) with CategoryRepository {

  protected def createInstance(entities: Map[CategoryId, Category]): This =
    new CategoryRepositoryOnMemory(entities)

}
