package com.github.j5ik2o.spetstore.domain.item

import com.github.j5ik2o.spetstore.infrastructure.support.Entity

/**
 * 商品区分のカテゴリを表すエンティティ。
 *
 * @param id 識別子
 * @param name 名前
 * @param description 説明
 */
case class Category
(id: CategoryId = CategoryId(),
 name: String,
 description: Option[String] = None)
  extends Entity[CategoryId]

