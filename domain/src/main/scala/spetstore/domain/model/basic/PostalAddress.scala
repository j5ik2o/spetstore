package spetstore.domain.model.basic

case class PostalAddress(zipCode: ZipCode,
                         pref: Pref,
                         cityName: String,
                         addressName: String,
                         buildingName: Option[String] = None)
