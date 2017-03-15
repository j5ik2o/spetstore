package com.github.j5ik2o.spetstore.domain.model.item

import com.github.j5ik2o.spetstore.domain.support.support.Entity
import com.github.j5ik2o.spetstore.domain.model.basic.StatusType

/**
 * カテゴリを表すエンティティ。
 *
 * @param id 識別子
 * @param name 名前
 * @param description 説明
 */
case class Category(
  id: CategoryId,
  status: StatusType.Value,
  name: String,
  description: Option[String] = None,
  version: Option[Long]
)
    extends Entity[CategoryId] {

  override def withVersion(version: Long): Entity[CategoryId] =
    copy(version = Some(version))

}

