package com.github.j5ik2o.spetstore.domain.account

/**
 * [[com.github.j5ik2o.spetstore.domain.account.Account]]の状態を表す値オブジェクト。
 */
object AccountStatus extends Enumeration {

  /**
   * 有効
   */
  val Enabled,

  /**
   * 無効
   */
  Disabled = Value

}
