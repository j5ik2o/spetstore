package com.github.j5ik2o.spetstore.domain.lifecycle.purchase

import com.github.j5ik2o.spetstore.domain.infrastructure.support.Repository
import com.github.j5ik2o.spetstore.domain.model.purchase.{Cart, CartId}

/**
 * [[com.github.j5ik2o.spetstore.domain.model.purchase.Cart]]のためのリポジトリ責務。
 */
trait CartRepository extends Repository[CartId, Cart] {

  type This = CartRepository

}

/**
 * コンパニオンオブジェクト。
 */
object CartRepository {

  /**
   * メモリ用リポジトリを生成する。
   *
   * @param entities エンティティの集合
   * @return [[com.github.j5ik2o.spetstore.domain.lifecycle.purchase.CartRepository]]
   */
  def ofMemory(entities: Map[CartId, Cart] = Map.empty): CartRepository =
    new CartRepositoryOnMemory(entities)

  /**
   * JDBC用リポジトリを生成する。
   *
   * @return [[com.github.j5ik2o.spetstore.domain.lifecycle.purchase.CartRepository]]
   */
  def ofJDBC: CartRepository = new CartRepositoryOnJDBC

}
