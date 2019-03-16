package spetstore.useCase

import akka.NotUsed
import akka.stream.scaladsl._
import monix.eval.Task
import monix.execution.Scheduler
import org.sisioh.baseunits.scala.timeutil.Clock
import spetstore.domain.model._
import spetstore.domain.model.basic.StatusType
import spetstore.useCase.model.{ CreateUserAccountRequest, CreateUserAccountResponse }
import spetstore.useCase.port.generator.IdGenerator
import spetstore.useCase.port.repository.UserAccountRepository
import wvlet.airframe._

trait UserAccountUseCase {

  private val userIdGenerator = bind[IdGenerator[UserAccountId]]
  private val userRepository  = bind[UserAccountRepository[Task]]

  def create(implicit scheduler: Scheduler): Flow[CreateUserAccountRequest, CreateUserAccountResponse, NotUsed] =
    Flow[CreateUserAccountRequest].mapAsync(1) { userAccount =>
      (for {
        id <- userIdGenerator.generateId()
        _ <- userRepository.store(
          UserAccount(
            id,
            StatusType.Active,
            EmailAddress(userAccount.emailAddress),
            HashedPassword(userAccount.password),
            userAccount.firstName,
            userAccount.lastName,
            Clock.now,
            None
          )
        )
      } yield CreateUserAccountResponse(id)).runAsync
    }

  def resolveById(id: UserAccountId)(implicit scheduler: Scheduler): Source[UserAccount, NotUsed] =
    Source.fromFuture {
      userRepository.resolveById(id).runAsync
    }

}
