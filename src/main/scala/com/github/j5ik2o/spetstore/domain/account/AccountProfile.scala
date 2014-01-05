package com.github.j5ik2o.spetstore.domain.account

import com.github.j5ik2o.spetstore.domain.address.{Contact, PostalAddress}

/**
 * [[com.github.j5ik2o.spetstore.domain.account.Account]]のプロフィールを表す値オブジェクト。
 *
 * @param postalAddress [[com.github.j5ik2o.spetstore.domain.address.PostalAddress]]
 * @param contact [[com.github.j5ik2o.spetstore.domain.address.Contact]]
 */
case class AccountProfile
(postalAddress: PostalAddress,
 contact: Contact)

