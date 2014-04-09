package com.github.j5ik2o.spetstore.domain.infrastructure.support

import com.github.j5ik2o.spetstore.infrastructure.db.CRUDMapper
import com.github.j5ik2o.spetstore.infrastructure.identifier.IdentifierService
import org.specs2.mutable.Specification
import scalikejdbc._, SQLInterpolation._

class RepositoryOnJDBCSpec extends Specification {

  sequential

  val identifierService = IdentifierService()

  case class PersonRecord(id: Long, firstName: String, lastName: String, version: Long)

  object PersonRecord extends CRUDMapper[PersonRecord] {

    override def defaultAlias = createAlias("person")

    override def tableName: String = "person"

    override def extract(rs: WrappedResultSet, n: SQLInterpolation.ResultName[PersonRecord]) = PersonRecord(
      id = rs.get(n.id),
      firstName = rs.get(n.firstName),
      lastName = rs.get(n.lastName),
      version = rs.get(n.version)
    )

    override def toNamedValues(record: PersonRecord): Seq[(Symbol, Any)] = Seq(
      'id -> record.id,
      'firstName -> record.firstName,
      'lastName -> record.lastName,
      'version -> record.version
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
      lastName = entity.lastName,
      version = entity.version.getOrElse(1)
    )

    override protected def convertToEntity(record: PersonRecord): Person = Person(
      id = PersonId(record.id),
      firstName = record.firstName,
      lastName = record.lastName,
      version = Some(record.version)
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
  last_name varchar(64),
  version bigint not null)
""".execute().apply()
  }

  def withContext[A](session: DBSession)(f: (EntityIOContext) => A): A =
    f(EntityIOContextOnJDBC(session))

  "repository" should {
    "store entity" in new PersonAutoRollback {
      withContext(session) {
        implicit ctx =>
          val idValue = identifierService.generate
          val personId = PersonId(idValue)
          val person = Person(personId, "Junichi", "Kato", None)
          val result = PersonRepositoryOnJDBC().store(person)
          result must beSuccessfulTry
//          val _person = Person(personId, "Junichi", "Kato", Some(1))
//          PersonRepositoryOnJDBC().store(_person)
      }
    }
    "contains a entity" in new PersonAutoRollback {
      withContext(session) {
        implicit ctx =>
          val idValue = identifierService.generate
          val personId = PersonId(idValue)
          val person = Person(personId, "Junichi", "Kato", None)
          val repository = PersonRepositoryOnJDBC()
          repository.store(person) must beSuccessfulTry
          repository.existById(personId) must beSuccessfulTry.like {
            case result =>
              result must beTrue
          }
      }
    }
    "get a entity" in new PersonAutoRollback {
      withContext(session) {
        implicit ctx =>
          val idValue = identifierService.generate
          val personId = PersonId(idValue)
          val person = Person(personId, "Junichi", "Kato", None)
          val repository = PersonRepositoryOnJDBC()
          repository.store(person) must beSuccessfulTry
          repository.resolveById(personId) must beSuccessfulTry.like {
            case entity =>
              entity must_== person
          }
      }
    }

    "delete a entity" in new PersonAutoRollback {
      withContext(session) {
        implicit ctx =>
          val idValue = identifierService.generate
          val personId = PersonId(idValue)
          val person = Person(personId, "Junichi", "Kato", None)
          val repository = PersonRepositoryOnJDBC()
          repository.store(person) must beSuccessfulTry
          repository.deleteById(personId) must beSuccessfulTry
      }
    }


  }

}
