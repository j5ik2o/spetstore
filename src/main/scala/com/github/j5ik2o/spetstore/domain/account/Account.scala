package com.github.j5ik2o.spetstore.domain.account

import com.github.j5ik2o.spetstore.infrastructure.support.Entity

/**
 * ペットストアの顧客を表すエンティティ。
 *
 * @param id 識別子
 * @param status [[com.github.j5ik2o.spetstore.domain.account.AccountStatus]]
 * @param name 名前
 * @param profile [[com.github.j5ik2o.spetstore.domain.account.AccountProfile]]
 * @param config [[com.github.j5ik2o.spetstore.domain.account.AccountConfig]]
 */
case class Account
(id: AccountId = AccountId(),
 status: AccountStatus.Value,
 name: String,
 profile: AccountProfile,
 config: AccountConfig)
  extends Entity[AccountId]




