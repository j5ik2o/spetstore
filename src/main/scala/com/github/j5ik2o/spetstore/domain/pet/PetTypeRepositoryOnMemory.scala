package com.github.j5ik2o.spetstore.domain.pet

import com.github.j5ik2o.spetstore.infrastructure.support.RepositoryOnMemory

/**
 * [[com.github.j5ik2o.spetstore.domain.pet.PetType]]のためのオンメモリリポジトリ。
 *
 * @param entities エンティティの集合
 */
private[pet]
class PetTypeRepositoryOnMemory(entities: Map[PetTypeId, PetType])
  extends RepositoryOnMemory[PetTypeId, PetType](entities) with PetTypeRepository {

  protected def createInstance(entities: Map[PetTypeId, PetType]): This =
    new PetTypeRepositoryOnMemory(entities)

}
