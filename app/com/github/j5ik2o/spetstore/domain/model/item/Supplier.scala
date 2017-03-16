package com.github.j5ik2o.spetstore.domain.model.item

import com.github.j5ik2o.spetstore.domain.support.support.Entity
import com.github.j5ik2o.spetstore.domain.model.basic.{ StatusType, Contact, PostalAddress }

/**
 * 仕入れ先を表すエンティティ。
 *
 * @param id [[com.github.j5ik2o.spetstore.domain.model.item.SupplierId]]
 * @param name 名前
 * @param postalAddress 住所
 * @param contact 連絡先
 */
case class Supplier(
  id: SupplierId,
  status: StatusType.Value,
  name: String,
  postalAddress: PostalAddress,
  contact: Contact,
  version: Option[Long]
)
    extends Entity[SupplierId] {

  override def canEqual(other: Any) = other.isInstanceOf[Supplier]

  override def withVersion(version: Long): Entity[SupplierId] =
    copy(version = Some(version))

}

