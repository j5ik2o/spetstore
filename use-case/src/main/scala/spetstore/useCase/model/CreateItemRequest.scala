package spetstore.useCase.model

case class CreateItemRequest(name: String, description: Option[String], categories: Set[String], price: Long)
