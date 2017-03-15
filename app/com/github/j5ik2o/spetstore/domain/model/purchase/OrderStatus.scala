package com.github.j5ik2o.spetstore.domain.model.purchase

/**
 * 注文状態を表す列挙型。
 */
object OrderStatus extends Enumeration {

  /**
   * 一次保留。
   */
  val Pending, /**
   * 承認済み。
   */ Approved, /**
   * キャンセル。
   */ Denied, /**
   *
   */ Completed = Value

}
