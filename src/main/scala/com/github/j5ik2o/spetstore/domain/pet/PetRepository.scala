package com.github.j5ik2o.spetstore.domain.pet

import com.github.j5ik2o.spetstore.infrastructure.support.Repository

/**
 * [[com.github.j5ik2o.spetstore.domain.pet.Pet]]のためのリポジトリ責務。
 */
trait PetRepository extends Repository[PetId, Pet] {

  type This = PetRepository

}

/**
 * コンパニオンオブジェクト。
 */
object PetRepository {

  /**
   * メモリ用リポジトリを生成する。
   *
   * @param entities エンティティの集合
   * @return [[com.github.j5ik2o.spetstore.domain.pet.PetRepository]]
   */
  def ofMemory(entities: Map[PetId, Pet] = Map.empty): PetRepository =
    new PetRepositoryOnMemory(entities)

  /**
   * JDBC用リポジトリを生成する。
   *
   * @return [[com.github.j5ik2o.spetstore.domain.pet.PetRepository]]
   */
  def ofJDBC: PetRepository =
    new PetRepositoryOnJDBC

}
