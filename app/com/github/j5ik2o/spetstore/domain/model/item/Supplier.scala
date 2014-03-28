package com.github.j5ik2o.spetstore.domain.model.item

import com.github.j5ik2o.spetstore.domain.model.basic.{StatusType, Contact, PostalAddress}
import com.github.j5ik2o.spetstore.domain.infrastructure.support.Entity

/**
 * 仕入れ先を表すエンティティ。
 *
 * @param id [[com.github.j5ik2o.spetstore.domain.model.item.SupplierId]]
 * @param name 名前
 * @param postalAddress 住所
 * @param contact 連絡先
 */
case class Supplier
(id: SupplierId,
 status: StatusType.Value,
 name: String,
 postalAddress: PostalAddress,
 contact: Contact)
  extends Entity[SupplierId]


