package com.github.j5ik2o.spetstore.domain.model.customer

/**
 * [[com.github.j5ik2o.spetstore.domain.model.customer.Customer]]の状態を表す値オブジェクト。
 */
object CustomerStatus extends Enumeration {

  /**
   * 有効
   */
  val Enabled,

  /**
   * 無効
   */
  Disabled = Value

}
