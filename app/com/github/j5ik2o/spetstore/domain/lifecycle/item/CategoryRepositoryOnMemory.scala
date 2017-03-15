package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.support.support.RepositoryOnMemory
import com.github.j5ik2o.spetstore.domain.model.item.{ Category, CategoryId }

/**
 * [[com.github.j5ik2o.spetstore.domain.lifecycle.item.CategoryRepository]]のためのオンメモリリポジトリ。
 *
 * @param entities エンティティの集合
 */
private[item] class CategoryRepositoryOnMemory(entities: Map[CategoryId, Category])
    extends RepositoryOnMemory[CategoryId, Category](entities) with CategoryRepository {

  protected def createInstance(entities: Map[CategoryId, Category]): This =
    new CategoryRepositoryOnMemory(entities)

}
