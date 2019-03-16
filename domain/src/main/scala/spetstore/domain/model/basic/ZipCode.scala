package spetstore.domain.model.basic

case class ZipCode private (areaCode: String, cityCode: String) {
  def asString = s"$areaCode-$cityCode"
}

object ZipCode {
  private val Regex = "^[0-9]{3}-[0-9]{4}$".r

  def apply(areaCode: String, cityCode: String): ZipCode = {
    apply(s"$areaCode-$cityCode")
  }

  def apply(zipCode: String): ZipCode = {
    require(Regex.findAllIn(zipCode).nonEmpty, "Invalid zipCode")
    val splits: Array[String] = zipCode.split("-")
    new ZipCode(splits(0), splits(1))
  }

}
