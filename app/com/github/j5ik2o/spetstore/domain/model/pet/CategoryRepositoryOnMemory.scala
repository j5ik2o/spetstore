package com.github.j5ik2o.spetstore.domain.model.pet

import com.github.j5ik2o.spetstore.domain.infrastructure.support.RepositoryOnMemory

/**
 * [[com.github.j5ik2o.spetstore.domain.model.pet.CategoryRepository]]のためのオンメモリリポジトリ。
 *
 * @param entities エンティティの集合
 */
private[pet]
class CategoryRepositoryOnMemory(entities: Map[CategoryId, Category])
  extends RepositoryOnMemory[CategoryId, Category](entities) with CategoryRepository {

  protected def createInstance(entities: Map[CategoryId, Category]): This =
    new CategoryRepositoryOnMemory(entities)

}
