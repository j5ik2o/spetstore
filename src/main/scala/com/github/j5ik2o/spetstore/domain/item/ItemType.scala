package com.github.j5ik2o.spetstore.domain.item

import com.github.j5ik2o.spetstore.infrastructure.support.Entity

/**
 * 商品区分を表すエンティティ。
 * 
 * @param id 識別子
 * @param categoryId [[com.github.j5ik2o.spetstore.domain.item.CategoryId]]
 * @param name 名前
 * @param description 説明
 */
case class ItemType
(id: ItemTypeId = ItemTypeId(),
 categoryId: CategoryId,
 name: String,
 description: Option[String] = None)
  extends Entity[ItemTypeId]

