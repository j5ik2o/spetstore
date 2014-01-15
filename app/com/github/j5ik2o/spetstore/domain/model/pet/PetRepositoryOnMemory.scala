package com.github.j5ik2o.spetstore.domain.model.pet

import com.github.j5ik2o.spetstore.domain.infrastructure.support.RepositoryOnMemory
import scala.util.Try

/**
 * [[com.github.j5ik2o.spetstore.domain.model.pet.Pet]]のためのオンメモリリポジトリ。
 *
 * @param entities エンティティの集合
 */
private[pet]
class PetRepositoryOnMemory(entities: Map[PetId, Pet])
  extends RepositoryOnMemory[PetId, Pet](entities) with PetRepository {

  protected def createInstance(entities: Map[PetId, Pet]): This =
    new PetRepositoryOnMemory(entities)

}
