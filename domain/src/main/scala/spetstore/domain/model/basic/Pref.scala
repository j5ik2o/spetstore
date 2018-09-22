package spetstore.domain.model.basic

import enumeratum._

import scala.collection.immutable

sealed trait Pref extends EnumEntry

object Pref extends Enum[Pref] {

  override def values: immutable.IndexedSeq[Pref] = findValues

  case object 北海道 extends Pref

  case object 青森県 extends Pref

  case object 岩手県 extends Pref

  case object 宮城県 extends Pref

  case object 秋田県 extends Pref

  case object 山形県 extends Pref

  case object 福島県 extends Pref

  case object 茨城県 extends Pref

  case object 栃木県 extends Pref

  case object 群馬県 extends Pref

  case object 埼玉県 extends Pref

  case object 千葉県 extends Pref

  case object 東京都 extends Pref

  case object 神奈川県 extends Pref

  case object 新潟県 extends Pref

  case object 富山県 extends Pref

  case object 石川県 extends Pref

  case object 福井県 extends Pref

  case object 山梨県 extends Pref

  case object 長野県 extends Pref

  case object 岐阜県 extends Pref

  case object 静岡県 extends Pref

  case object 愛知県 extends Pref

  case object 三重県 extends Pref

  case object 滋賀県 extends Pref

  case object 京都府 extends Pref

  case object 大阪府 extends Pref

  case object 兵庫県 extends Pref

  case object 奈良県 extends Pref

  case object 和歌山県 extends Pref

  case object 鳥取県 extends Pref

  case object 島根県 extends Pref

  case object 岡山県 extends Pref

  case object 広島県 extends Pref

  case object 山口県 extends Pref

  case object 徳島県 extends Pref

  case object 香川県 extends Pref

  case object 愛媛県 extends Pref

  case object 高知県 extends Pref

  case object 福岡県 extends Pref

  case object 佐賀県 extends Pref

  case object 長崎県 extends Pref

  case object 熊本県 extends Pref

  case object 大分県 extends Pref

  case object 宮崎県 extends Pref

  case object 鹿児島県 extends Pref

  case object 沖縄県 extends Pref

}
