package com.github.j5ik2o.spetstore.domain.account

import com.github.j5ik2o.spetstore.infrastructure.support.RepositoryOnMemory

/**
 * [[com.github.j5ik2o.spetstore.domain.account.AccountRepository]]のためのオンメモリリポジトリ。
 *
 * @param entities エンティティの集合
 */
private[account]
class AccountRepositoryOnMemory(entities: Map[AccountId, Account])
extends RepositoryOnMemory[AccountId, Account](entities) with AccountRepository {

  protected def createInstance(entities: Map[AccountId, Account]): This =
    new AccountRepositoryOnMemory(entities)

}
