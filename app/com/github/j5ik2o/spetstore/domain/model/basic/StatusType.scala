package com.github.j5ik2o.spetstore.domain.model.basic

/**
 * [[com.github.j5ik2o.spetstore.domain.model.customer.Customer]]の状態を表す値オブジェクト。
 */
object StatusType extends Enumeration {

  /**
   * 有効
   */
  val Enabled, /**
   * 無効
   */ Disabled = Value

}
