package com.github.j5ik2o.spetstore.domain.lifecycle.purchase

import com.github.j5ik2o.spetstore.domain.infrastructure.support.Repository
import com.github.j5ik2o.spetstore.domain.model.purchase.{Order, OrderId}
import com.github.j5ik2o.spetstore.domain.lifecycle.purchase

/**
 * [[com.github.j5ik2o.spetstore.domain.model.purchase.Order]]のためのリポジトリ責務。
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
   * @return [[purchase.OrderRepository]]
   */
  def ofMemory(entities: Map[OrderId, Order] = Map.empty): OrderRepository =
    new OrderRepositoryOnMemory(entities)

  /**
   * JDBC用リポジトリを生成する。
   *
   * @return [[purchase.OrderRepository]]
   */
  def ofJDBC: OrderRepository = new OrderRepositoryOnJDBC

}


