package com.github.j5ik2o.spetstore.domain.pet

import com.github.j5ik2o.spetstore.infrastructure.support.RepositoryOnMemory

/**
 * [[com.github.j5ik2o.spetstore.domain.pet.Pet]]のためのオンメモリリポジトリ。
 *
 * @param entities エンティティの集合
 */
private[pet]
class PetRepositoryOnMemory(entities: Map[PetId, Pet])
  extends RepositoryOnMemory[PetId, Pet](entities) with PetRepository {

  protected def createInstance(entities: Map[PetId, Pet]): This =
    new PetRepositoryOnMemory(entities)

}
