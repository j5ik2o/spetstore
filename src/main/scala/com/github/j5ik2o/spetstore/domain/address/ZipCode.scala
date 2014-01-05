package com.github.j5ik2o.spetstore.domain.address

/**
 * 郵便番号を表す値オブジェクト。
 *
 * @param areaCode 郵便区番号
 * @param cityCode 町域番号
 */
case class ZipCode(areaCode: String, cityCode: String) {
  require(areaCode.size <= 3)
  require(cityCode.size <= 4)
  def asString = s"$areaCode-$cityCode"
}

object ZipCode {

  def apply(zipCode: String): ZipCode = {
    val splits = zipCode.split("-")
    ZipCode(splits(0), splits(1))
  }

}
