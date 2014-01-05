package com.github.j5ik2o.spetstore.domain.account

import com.github.j5ik2o.spetstore.infrastructure.support.Repository

/**
 * [[com.github.j5ik2o.spetstore.domain.account.Account]]のためのリポジトリ責務。
 */
trait AccountRepository extends Repository[AccountId, Account] {

  type This = AccountRepository

}

/**
 * コンパニオンオブジェクト。
 */
object AccountRepository {

  /**
   * オンメモリリポジトリを生成する。
   *
   * @param entities エンティティの集合
   * @return [[com.github.j5ik2o.spetstore.domain.account.AccountRepository]]
   */
  def ofMemory(entities: Map[AccountId, Account]): AccountRepository =
    new AccountRepositoryOnMemory(entities)

  /**
   * JDBC用リポジトリを生成する。
   *
   * @return [[com.github.j5ik2o.spetstore.domain.account.AccountRepository]]
   */
  def ofJDBC: AccountRepository =
    new AccountRepositoryOnJDBC

}
