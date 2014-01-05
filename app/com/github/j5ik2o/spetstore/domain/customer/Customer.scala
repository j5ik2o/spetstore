package com.github.j5ik2o.spetstore.domain.customer

import com.github.j5ik2o.spetstore.infrastructure.support.Entity

/**
 * ペットストアの顧客を表すエンティティ。
 *
 * @param id 識別子
 * @param status [[com.github.j5ik2o.spetstore.domain.customer.CustomerStatus]]
 * @param name 名前
 * @param profile [[com.github.j5ik2o.spetstore.domain.customer.CustomerProfile]]
 * @param config [[com.github.j5ik2o.spetstore.domain.customer.CustomerConfig]]
 */
case class Customer
(id: CustomerId = CustomerId(),
 status: CustomerStatus.Value,
 name: String,
 profile: CustomerProfile,
 config: CustomerConfig)
  extends Entity[CustomerId]




