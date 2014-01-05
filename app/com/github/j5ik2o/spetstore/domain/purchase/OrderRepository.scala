package com.github.j5ik2o.spetstore.domain.purchase

import com.github.j5ik2o.spetstore.infrastructure.support.Repository

/**
 * [[com.github.j5ik2o.spetstore.domain.purchase.Order]]のためのリポジトリ責務。
 */
trait OrderRepository extends Repository[OrderId, Order] {

  type This = OrderRepository

}

/**
 * コンパニオンオブジェクト。
 */
object OrderRepository {

  /**
   * メモリ用リポジトリを生成する。
   *
   * @param entities エンティティのマップ
   * @return [[com.github.j5ik2o.spetstore.domain.purchase.OrderRepository]]
   */
  def ofMemory(entities: Map[OrderId, Order]): OrderRepository =
    new OrderRepositoryOnMemory(entities)

  /**
   * JDBC用リポジトリを生成する。
   *
   * @return [[com.github.j5ik2o.spetstore.domain.purchase.OrderRepository]]
   */
  def ofJDBC: OrderRepository = new OrderRepositoryOnJDBC

}


