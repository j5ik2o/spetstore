package com.github.j5ik2o.spetstore.domain.lifecycle.customer

import com.github.j5ik2o.spetstore.domain.support.support.{ MultiIOSupport, EntityIOContext, Repository }
import com.github.j5ik2o.spetstore.domain.model.customer.{ Customer, CustomerId }
import scala.util.Try

/**
 * [[com.github.j5ik2o.spetstore.domain.model.customer.Customer]]のためのリポジトリ責務。
 */
trait CustomerRepository extends Repository[CustomerId, Customer] with MultiIOSupport[CustomerId, Customer] {

  type This = CustomerRepository

  /**
   * 指定したログイン名に該当する顧客を解決する。
   *
   * @param loginName ログイン名
   * @param ctx [[com.github.j5ik2o.spetstore.domain.support.support.EntityIOContext]]
   * @return `Try`にラップされた[[com.github.j5ik2o.spetstore.domain.model.customer.Customer]]
   */
  def resolveByLoginName(loginName: String)(implicit ctx: EntityIOContext): Try[Option[Customer]]

}

/**
 * コンパニオンオブジェクト。
 */
object CustomerRepository {

  /**
   * メモリ用リポジトリを生成する。
   *
   * @param entities エンティティの集合
   * @return [[CustomerRepository]]
   */
  def ofMemory(entities: Map[CustomerId, Customer] = Map.empty): CustomerRepository =
    new CustomerRepositoryOnMemory(entities)

  /**
   * JDBC用リポジトリを生成する。
   *
   * @return [[CustomerRepository]]
   */
  def ofJDBC: CustomerRepository =
    new CustomerRepositoryOnJDBC

}
