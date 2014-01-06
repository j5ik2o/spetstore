package com.github.j5ik2o.spetstore.domain.pet

import com.github.j5ik2o.spetstore.domain.basic.{Contact, PostalAddress}
import com.github.j5ik2o.spetstore.infrastructure.support.Entity

/**
 * 仕入れ先を表すエンティティ。
 *
 * @param id [[com.github.j5ik2o.spetstore.domain.pet.SupplierId]]
 * @param name 名前
 * @param postalAddress 住所
 * @param contact 連絡先
 */
case class Supplier
(id: SupplierId,
 name: String,
 postalAddress: PostalAddress,
 contact: Contact)
  extends Entity[SupplierId]


