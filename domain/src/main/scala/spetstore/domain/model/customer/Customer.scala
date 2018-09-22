package spetstore.domain.model.customer

import java.time.ZonedDateTime

import com.github.j5ik2o.dddbase.{ Aggregate, AggregateLongId }
import spetstore.domain.model.basic.StatusType

import scala.reflect.{ classTag, ClassTag }

case class CustomerId(value: Long) extends AggregateLongId

case class Customer(id: CustomerId,
                    status: StatusType,
                    name: String,
                    createdAt: ZonedDateTime,
                    updatedAt: Option[ZonedDateTime])
    extends Aggregate {
  override type IdType        = CustomerId
  override type AggregateType = Customer
  override protected val tag: ClassTag[Customer] = classTag[Customer]
}
