package com.github.j5ik2o.spetstore.domain.infrastructure.support

import org.specs2.mutable.Specification
import scalikejdbc._, SQLInterpolation._
import com.github.j5ik2o.spetstore.infrastructure.db.CRUDMapper
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService

class RepositoryOnJDBCSpec extends Specification {

  sequential

  val identifierService = IdentifierService()

  case class PersonRecord(id: Long, firstName: String, lastName: String)

  object PersonRecord extends CRUDMapper[PersonRecord] {

    override def defaultAlias = createAlias("person")

    override def tableName: String = "person"

    override def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[PersonRecord]) = PersonRecord(
      id = rs.get(n.id),
      firstName = rs.get(n.firstName),
      lastName = rs.get(n.lastName)
    )

    override def toNamedValues(record: PersonRecord): Seq[(Symbol, Any)] = Seq(
      'id -> record.id,
      'firstName -> record.firstName,
      'lastName -> record.lastName
    )

  }

  case class PersonRepositoryOnJDBC()
    extends SimpleRepositoryOnJDBC[PersonId, Person] with PersonRepository {

    override type This = PersonRepositoryOnJDBC

    override type T = PersonRecord

    override protected val mapper = PersonRecord

    override protected def convertToRecord(entity: Person) = PersonRecord(
      id = entity.id.value,
      firstName = entity.firstName,
      lastName = entity.lastName
    )

    override protected def convertToEntity(record: T): Person = Person(
      id = PersonId(record.id),
      firstName = record.firstName,
      lastName = record.lastName
    )
  }


  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:mem:test", "user", "pass")

  DB autoCommit {
    implicit s =>
      sql"""
create table person (
  pk bigint not null auto_increment primary key,
  id bigint not null unique,
  first_name varchar(64),
  last_name varchar(64)
)
""".execute().apply()
  }
  val idValue = identifierService.generate
  def withContext[A](session: DBSession)(f: (EntityIOContext) => A): A =
    f(EntityIOContextOnJDBC(session))

  "repository" should {
    "store entity" in new PersonAutoRollback {
      withContext(session) {
        implicit ctx =>
          val personId = PersonId(idValue)
          val person = Person(personId, "Junichi", "Kato")
          PersonRepositoryOnJDBC().storeEntity(person) must beSuccessfulTry
      }
    }
    "contains a entity" in new PersonAutoRollback {
      withContext(session) {
        implicit ctx =>
          val personId = PersonId(idValue)
          val person = Person(personId, "Junichi", "Kato")
          val repository = PersonRepositoryOnJDBC()
          repository.storeEntity(person) must beSuccessfulTry
          repository.existByIdentifier(personId) must beSuccessfulTry.like {
            case result =>
              result must beTrue
          }
      }
    }
    "get a entity" in new PersonAutoRollback {
      withContext(session) {
        implicit ctx =>
          val personId = PersonId(idValue)
          val person = Person(personId, "Junichi", "Kato")
          val repository = PersonRepositoryOnJDBC()
          repository.storeEntity(person) must beSuccessfulTry
          repository.resolveEntity(personId) must beSuccessfulTry.like {
            case entity =>
              entity must_== person
          }
      }
    }

    "delete a entity" in new PersonAutoRollback {
      withContext(session) {
        implicit ctx =>
          val personId = PersonId(idValue)
          val person = Person(personId, "Junichi", "Kato")
          val repository = PersonRepositoryOnJDBC()
          repository.storeEntity(person) must beSuccessfulTry
          repository.deleteByIdentifier(personId) must beSuccessfulTry
      }
    }


  }

}
