package com.github.j5ik2o.spetstore.domain.customer

import com.github.j5ik2o.spetstore.domain.address.{Contact, PostalAddress}

/**
 * [[com.github.j5ik2o.spetstore.domain.customer.Customer]]のプロフィールを表す値オブジェクト。
 *
 * @param postalAddress [[com.github.j5ik2o.spetstore.domain.address.PostalAddress]]
 * @param contact [[com.github.j5ik2o.spetstore.domain.address.Contact]]
 */
case class CustomerProfile
(postalAddress: PostalAddress,
 contact: Contact)

