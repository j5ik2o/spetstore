package com.github.j5ik2o.spetstore.domain.lifecycle.purchase

import com.github.j5ik2o.spetstore.domain.support.support.{ MultiIOSupport, Repository }
import com.github.j5ik2o.spetstore.domain.model.purchase.{ Cart, CartId }

/**
 * [[Cart]]のためのリポジトリ責務。
 */
trait CartRepository extends Repository[CartId, Cart] with MultiIOSupport[CartId, Cart] {

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
   * @return [[CartRepository]]
   */
  def ofMemory(entities: Map[CartId, Cart] = Map.empty): CartRepository =
    new CartRepositoryOnMemory(entities)

  /**
   * JDBC用リポジトリを生成する。
   *
   * @return [[CartRepository]]
   */
  def ofJDBC: CartRepository = new CartRepositoryOnJDBC

}
