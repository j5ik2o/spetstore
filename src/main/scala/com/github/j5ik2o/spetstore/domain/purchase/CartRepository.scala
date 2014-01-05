package com.github.j5ik2o.spetstore.domain.purchase

import com.github.j5ik2o.spetstore.infrastructure.support.Repository

/**
 * [[com.github.j5ik2o.spetstore.domain.purchase.Cart]]のためのリポジトリ責務。
 */
trait CartRepository extends Repository[CartId, Cart] {

  type This = CartRepository

}

/**
 * コンパニオンオブジェクト。
 */
object CartRepository {

  /**
   * オンメモリリポジトリを生成する。
   *
   * @param entities エンティティの集合
   * @return [[com.github.j5ik2o.spetstore.domain.purchase.CartRepository]]
   */
  def ofMemory(entities: Map[CartId, Cart]): CartRepository =
    new CartRepositoryOnMemory(entities)

}
