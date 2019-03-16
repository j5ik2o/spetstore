package spetstore.interface.api.model

case class ResolveItemResponseBody(id: String,
                                   name: String,
                                   description: Option[String],
                                   categories: Set[String],
                                   price: Long,
                                   createdAt: Long,
                                   updatedAt: Long)
