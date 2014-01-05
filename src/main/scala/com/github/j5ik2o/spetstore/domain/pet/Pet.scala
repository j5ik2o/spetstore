package com.github.j5ik2o.spetstore.domain.pet

import com.github.j5ik2o.spetstore.infrastructure.support.{EntityIOContext, Entity}
import scala.util.Try

/**
 * 商品を表すエンティティ。
 *
 * TODO 商品と在庫の概念を分ける
 * 
 * @param id 識別子
 * @param petTypeId [[com.github.j5ik2o.spetstore.domain.pet.PetTypeId]]
 * @param name 名前
 * @param description 説明
 * @param price 価格
 * @param quantity 在庫数量
 */
case class Pet
(id: PetId = PetId(),
 petTypeId: PetTypeId,
 name: String,
 description: Option[String] = None,
 price: BigDecimal,
 quantity: Int = 1)
  extends Entity[PetId] {

  /**
   * [[com.github.j5ik2o.spetstore.domain.pet.PetType]]を取得する。
   *
   * @param ptr [[com.github.j5ik2o.spetstore.domain.pet.PetTypeRepository]]
   * @param ctx [[com.github.j5ik2o.spetstore.infrastructure.support.EntityIOContext]]
   * @return `Try`にラップされた[[com.github.j5ik2o.spetstore.domain.pet.PetType]]
   */
  def petType(implicit ptr: PetTypeRepository, ctx: EntityIOContext): Try[PetType] =
    ptr.resolve(petTypeId)

}



