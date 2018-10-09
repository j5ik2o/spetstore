package spetstore.interface.api.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "course")
case class CreateItemRequestJson(@Schema(description = "name", maxLength = 255)
                                 name: String,
                                 @Schema(description = "description", maxLength = 255)
                                 description: Option[String],
                                 @Schema(description = "categories", maxLength = 255)
                                 categories: Set[String],
                                 @Schema(description = "price")
                                 price: Long)
