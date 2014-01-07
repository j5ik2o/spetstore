package com.github.j5ik2o.spetstore.domain.model.pet

import com.github.j5ik2o.spetstore.domain.infrastructure.support.Entity

/**
 * ペットの品種を表すエンティティ。
 * 
 * @param id 識別子
 * @param categoryId [[com.github.j5ik2o.spetstore.domain.model.pet.CategoryId]]
 * @param name 名前
 * @param description 説明
 */
case class PetType
(id: PetTypeId = PetTypeId(),
 categoryId: CategoryId,
 name: String,
 description: Option[String] = None)
  extends Entity[PetTypeId]

