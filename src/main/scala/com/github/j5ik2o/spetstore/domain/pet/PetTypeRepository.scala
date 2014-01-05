package com.github.j5ik2o.spetstore.domain.pet

import com.github.j5ik2o.spetstore.infrastructure.support.Repository

/**
 * [[com.github.j5ik2o.spetstore.domain.pet.PetType]]のためのリポジトリ責務。
 */
trait PetTypeRepository extends Repository[PetTypeId, PetType] {

  type This = PetTypeRepository

}

/**
 * コンパニオンオブジェクト。
 */
object PetTypeRepository {

  /**
   * メモリ用リポジトリを生成する。
   *
   * @param entities エンティティのマップ
   * @return [[com.github.j5ik2o.spetstore.domain.pet.PetTypeRepository]]
   */
  def ofMemory(entities: Map[PetTypeId, PetType] = Map.empty): PetTypeRepository =
    new PetTypeRepositoryOnMemory(entities)

  /**
   * JDBC用リポジトリを生成する。
   *
   * @return [[com.github.j5ik2o.spetstore.domain.pet.PetTypeRepository]]
   */
  def ofJDBC: PetTypeRepository =
    new PetTypeRepositoryOnJDBC

}

