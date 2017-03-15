package com.github.j5ik2o.spetstore.domain.model.customer

import com.github.j5ik2o.spetstore.domain.model.basic.{ Contact, PostalAddress }

/**
 * [[com.github.j5ik2o.spetstore.domain.model.customer.Customer]]のプロフィールを表す値オブジェクト。
 *
 * @param postalAddress [[com.github.j5ik2o.spetstore.domain.model.basic.PostalAddress]]
 * @param contact [[com.github.j5ik2o.spetstore.domain.model.basic.Contact]]
 */
case class CustomerProfile(
  postalAddress: PostalAddress,
  contact: Contact
)

