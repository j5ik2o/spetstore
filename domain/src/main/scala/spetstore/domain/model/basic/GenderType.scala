package spetstore.domain.model.basic

import enumeratum._

import scala.collection.immutable

sealed abstract class GenderType(override val entryName: String) extends EnumEntry

object GenderType extends Enum[GenderType] {

  override def values: immutable.IndexedSeq[GenderType] = findValues

  case object Male extends GenderType("m")

  case object Female extends GenderType("f")

}
