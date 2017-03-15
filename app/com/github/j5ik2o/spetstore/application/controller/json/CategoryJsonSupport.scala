package com.github.j5ik2o.spetstore.application.controller.json

import com.github.j5ik2o.spetstore.domain.model.basic.StatusType
import com.github.j5ik2o.spetstore.domain.model.item.{ Category, CategoryId }
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService

/**
 * [[Category]]のJSON表現を表したモデル。
 *
 * @param id          ID
 * @param name        名前
 * @param description 説明
 */
case class CategoryJson(
  id: Option[String],
  name: String,
  description: Option[String],
  version: Option[Long]
)

/**
 * [[CategoryJson]]のためのトレイト。
 */
trait CategoryJsonSupport {

  val identifierService: IdentifierService

  protected def convertToEntity(json: CategoryJson): Category =
    Category(
      id = CategoryId(json.id.map(_.toLong).get),
      status = StatusType.Enabled,
      name = json.name,
      description = json.description,
      version = json.version
    )

  protected def convertToEntityWithoutId(json: CategoryJson): Category =
    Category(
      id = CategoryId(identifierService.generate),
      status = StatusType.Enabled,
      name = json.name,
      description = json.description,
      version = json.version
    )

  protected def convertToJson(entity: Category): CategoryJson = CategoryJson(
    id = Some(entity.id.toString),
    name = entity.name,
    description = entity.description,
    version = entity.version
  )

}
