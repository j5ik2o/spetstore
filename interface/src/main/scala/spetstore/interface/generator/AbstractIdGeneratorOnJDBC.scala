package spetstore.interface.generator

import monix.eval.Task
import slick.jdbc.JdbcProfile
import spetstore.useCase.port.generator.IdGenerator

abstract class AbstractIdGeneratorOnJDBC[ID](val profile: JdbcProfile,
                                             val db: JdbcProfile#Backend#Database,
                                             val tableName: String)
    extends IdGenerator[ID] {

  import profile.api._

  protected def internalGenerateId(): Task[Long] = Task.deferFutureAction { implicit ec =>
    val action = for {
      updateResult <- sqlu"UPDATE #${tableName} SET id = LAST_INSERT_ID(id+1)"
      _            <- if (updateResult == 1) DBIO.successful(Some(updateResult)) else DBIO.successful(None)
      selectResult <- sql"SELECT LAST_INSERT_ID() AS id".as[Long].head
    } yield selectResult
    db.run(action.transactionally)
  }

}
