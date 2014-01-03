package com.j5ik2o.spetstore.domain.account

import com.j5ik2o.spetstore.infrastructure.support.Repository

/**
 * [[com.j5ik2o.spetstore.domain.account.Account]]のためのリポジトリ責務。
 */
trait AccountRepository extends Repository[AccountId, Account]

/**
 * コンパニオンオブジェクト。
 */
object AccountRepository {

  /**
   * オンメモリリポジトリを生成する。
   *
   * @param entities エンティティの集合
   * @return [[com.j5ik2o.spetstore.domain.account.AccountRepository]]
   */
  def ofMemory(entities: Map[AccountId, Account]): AccountRepository =
    new AccountRepositoryOnMemory(entities)

  /**
   * JDBC用リポジトリを生成する。
   *
   * @return [[com.j5ik2o.spetstore.domain.account.AccountRepository]]
   */
  def ofJDBC: AccountRepository =
    new AccountRepositoryOnJDBC

}
