package com.github.j5ik2o.spetstore.infrastructure.support

import java.util.UUID
import org.specs2.mutable.Specification
import scalikejdbc._, SQLInterpolation._

class RepositoryOnJDBCSpec extends Specification {

  sequential

  case class PersonRepositoryOnJDBC()
    extends RepositoryOnJDBC[PersonId, Person] with PersonRepository {

    type This = PersonRepository

    override val tableName = "person"

    override val columnNames = Seq("id", "first_name", "last_name")

    protected def convertResultSetToEntity(resultSet: WrappedResultSet): Person =
      Person(
        id = PersonId(UUID.fromString(resultSet.string("id"))),
        firstName = resultSet.string("first_name"),
        lastName = resultSet.string("last_name")
      )

    protected def convertEntityToValues(entity: Person): Seq[Any] =
      Seq(entity.id.value.toString, entity.firstName, entity.lastName)

  }

  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:mem:test", "user", "pass")
  implicit val session = AutoSession
  implicit val ctx = EntityIOContextOnJDBC(session)

  sql"""
create table person (
  id varchar(64) not null primary key,
  first_name varchar(64),
  last_name varchar(64)
)
""".execute().apply()

  "repository" should {
    "store entity" in {
      val personId = PersonId()
      val person = Person(personId, "Junichi", "Kato")
      PersonRepositoryOnJDBC().store(person) must beSuccessfulTry
    }
    "contains a entity" in {
      val personId = PersonId()
      val person = Person(personId, "Junichi", "Kato")
      val repository = PersonRepositoryOnJDBC()
      repository.store(person) must beSuccessfulTry
      repository.contains(personId) must beSuccessfulTry.like {
        case result =>
          result must beTrue
      }
    }
    "get a entity" in {
      val personId = PersonId()
      val person = Person(personId, "Junichi", "Kato")
      val repository = PersonRepositoryOnJDBC()
      repository.store(person) must beSuccessfulTry
      repository.resolve(personId) must beSuccessfulTry.like {
        case entity =>
          entity must_== person
      }
    }

    "delete a entity" in {
      val personId = PersonId()
      val person = Person(personId, "Junichi", "Kato")
      val repository = PersonRepositoryOnJDBC()
      repository.store(person) must beSuccessfulTry
      repository.delete(personId) must beSuccessfulTry
    }


  }

}
