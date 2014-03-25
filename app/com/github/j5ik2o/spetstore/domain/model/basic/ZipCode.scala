package com.github.j5ik2o.spetstore.domain.model.basic

/**
 * 郵便番号を表す値オブジェクト。
 *
 * @param areaCode 郵便区番号
 * @param cityCode 町域番号
 */
case class ZipCode(areaCode: String, cityCode: String) {
  //require(areaCode.size <= 3)
  //require(cityCode.size <= 4)
  def asString = s"$areaCode-$cityCode"
}

/**
 * コンパニオンオブジェクト。
 */
object ZipCode {

  /**
   * 000-0000形式の郵便番号文字列から
   * [[com.github.j5ik2o.spetstore.domain.model.basic.ZipCode]]
   * を生成する。
   *
   * @param zipCode 000-0000形式の郵便番号文字列
   * @return [[com.github.j5ik2o.spetstore.domain.model.basic.ZipCode]]
   */
  def apply(zipCode: String): ZipCode = {
    val splits = zipCode.split("-").ensuring(_.size == 2)
    ZipCode(splits(0), splits(1))
  }

}
