package spetstore.domain.model.basic

class ZipCode(areaCode: String, cityCode: String) {
  def asString = s"$areaCode-$cityCode"
}

object ZipCode {

  def apply(areaCode: String, cityCode: String): ZipCode = new ZipCode(areaCode, cityCode)

  def apply(zipCode: String): ZipCode = {
    val splits: Array[String] = zipCode.split("-")
    new ZipCode(splits(0), splits(1))
  }

}
