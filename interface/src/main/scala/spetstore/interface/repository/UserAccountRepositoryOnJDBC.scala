package spetstore.interface.repository

import com.github.j5ik2o.dddbase.slick.AggregateIOBaseFeature.RIO
import com.github.j5ik2o.dddbase.slick._
import monix.eval.Task
import org.sisioh.baseunits.scala.time.TimePoint
import slick.jdbc.JdbcProfile
import spetstore.domain.model._
import spetstore.domain.model.basic.StatusType
import spetstore.interface.dao.UserAccountComponent
import spetstore.useCase.port.repository.UserAccountRepository

class UserAccountRepositoryOnJDBC(val profile: JdbcProfile, val db: JdbcProfile#Backend#Database)
    extends UserAccountRepository[Task]
    with AggregateSingleReadFeature
    with AggregateMultiReadFeature
    with AggregateSingleWriteFeature
    with AggregateMultiWriteFeature
    with AggregateSingleSoftDeleteFeature
    with AggregateMultiSoftDeleteFeature
    with UserAccountComponent {

  override type RecordType = UserAccountRecord
  override type TableType  = UserAccounts
  override protected val dao = UserAccountDao

  override protected def convertToAggregate: UserAccountRecord => RIO[UserAccount] = { record =>
    Task.pure {
      UserAccount(
        id = UserAccountId(record.id),
        status = StatusType.withName(record.status),
        emailAddress = EmailAddress(record.emailAddress),
        password = HashedPassword(record.hashedPassword),
        firstName = record.firstName,
        lastName = record.lastName,
        createdAt = TimePoint.from(record.createdAt.toInstant),
        updatedAt = record.updatedAt.map(v => TimePoint.from(v))
      )
    }
  }

  override protected def convertToRecord: UserAccount => RIO[UserAccountRecord] = { aggregate =>
    Task.pure {
      UserAccountRecord(
        id = aggregate.id.value,
        status = aggregate.status.entryName,
        emailAddress = aggregate.emailAddress.value,
        hashedPassword = aggregate.password.value,
        firstName = aggregate.firstName,
        lastName = aggregate.lastName,
        createdAt = aggregate.createdAt.asJavaZonedDateTime(),
        updatedAt = aggregate.updatedAt.map(_.asJavaZonedDateTime())
      )
    }
  }

}
