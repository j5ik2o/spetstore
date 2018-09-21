package spetstore.domain.model

import com.github.j5ik2o.dddbase.{Aggregate, AggregateLongId}

import scala.reflect._

case class PetId(value: Long) extends AggregateLongId

case class Pet(id: PetId, name: String) extends Aggregate{
  override type IdType = PetId
  override type AggregateType = Pet
  override protected val tag: ClassTag[Pet] = classTag[Pet]
}
