package com.github.j5ik2o.spetstore.domain.model.item

import com.github.j5ik2o.spetstore.domain.support.support.Entity
import com.github.j5ik2o.spetstore.domain.model.basic.StatusType

/**
 * 商品の種類を表すエンティティ。
 *
 * @param id 識別子
 * @param categoryId [[com.github.j5ik2o.spetstore.domain.model.item.CategoryId]]
 * @param name 名前
 * @param description 説明
 */
case class ItemType(
  id: ItemTypeId,
  status: StatusType.Value,
  categoryId: CategoryId,
  name: String,
  description: Option[String] = None,
  version: Option[Long]
)
    extends Entity[ItemTypeId] {

  override def withVersion(version: Long): Entity[ItemTypeId] =
    copy(version = Some(version))

}

