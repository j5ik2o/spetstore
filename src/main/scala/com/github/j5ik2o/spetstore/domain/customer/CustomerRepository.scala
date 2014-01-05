package com.github.j5ik2o.spetstore.domain.customer

import com.github.j5ik2o.spetstore.infrastructure.support.{EntityIOContext, Repository}
import scala.util.Try

/**
 * [[com.github.j5ik2o.spetstore.domain.customer.Customer]]のためのリポジトリ責務。
 */
trait CustomerRepository extends Repository[CustomerId, Customer] {

  type This = CustomerRepository

  def resolveByName(name: String)(implicit ctx: EntityIOContext): Try[Customer]

}

/**
 * コンパニオンオブジェクト。
 */
object CustomerRepository {

  /**
   * メモリ用リポジトリを生成する。
   *
   * @param entities エンティティの集合
   * @return [[com.github.j5ik2o.spetstore.domain.customer.CustomerRepository]]
   */
  def ofMemory(entities: Map[CustomerId, Customer]): CustomerRepository =
    new CustomerRepositoryOnMemory(entities)

  /**
   * JDBC用リポジトリを生成する。
   *
   * @return [[com.github.j5ik2o.spetstore.domain.customer.CustomerRepository]]
   */
  def ofJDBC: CustomerRepository =
    new CustomerRepositoryOnJDBC

}
