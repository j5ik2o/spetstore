package controllers.models

case class ItemJson(id: Long,
                    name: String,
                    description: Option[String],
                    categories: Set[String],
                    price: Long,
                    createdAt: Long,
                    updatedAt: Option[Long])
