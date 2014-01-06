package com.github.j5ik2o.spetstore.domain.customer

import com.github.j5ik2o.spetstore.domain.basic.Contact
import com.github.j5ik2o.spetstore.domain.basic.PostalAddress
import com.github.j5ik2o.spetstore.domain.basic._
import com.github.j5ik2o.spetstore.domain.pet.CategoryId
import com.github.j5ik2o.spetstore.infrastructure.support.{EntityNotFoundException, EntityIOContext, RepositoryOnJDBC}
import java.util.UUID
import scala.util.Try
import scalikejdbc.WrappedResultSet
import scalikejdbc._, SQLInterpolation._

private[customer]
class CustomerRepositoryOnJDBC
  extends RepositoryOnJDBC[CustomerId, Customer] with CustomerRepository {

  override val tableName = "customer"

  override val columnNames = Seq(
    "id",
    "name",
    "status",
    "zip_code",
    "pref_code",
    "city_name",
    "address_name",
    "building_name",
    "email",
    "phone",
    "login_name",
    "password",
    "favorite_category_id"
  )

  protected def convertEntityToValues(entity: Customer): Seq[Any] = Seq(
    entity.id.value.toString,
    entity.name,
    entity.status.id,
    entity.profile.postalAddress.zipCode.asString,
    entity.profile.postalAddress.pref.id,
    entity.profile.postalAddress.cityName,
    entity.profile.postalAddress.addressName,
    entity.profile.postalAddress.buildingName,
    entity.profile.contact.email,
    entity.profile.contact.phone,
    entity.config.loginName,
    entity.config.password,
    entity.config.favoriteCategoryId.map(_.value.toString)
  )

  protected def convertResultSetToEntity(resultSet: WrappedResultSet): Customer =
    Customer(
      id = CustomerId(UUID.fromString(resultSet.string("id"))),
      name = resultSet.string("name"),
      sexType = resultSet.intOpt("sex_type").map(SexType(_)),
      status = CustomerStatus(resultSet.int("status")),
      profile = CustomerProfile(
        postalAddress = PostalAddress(
          zipCode = ZipCode(resultSet.string("zip_code")),
          pref = Pref(resultSet.int("pref_code")),
          cityName = resultSet.string("city_name"),
          addressName = resultSet.string("address_name"),
          buildingName = resultSet.stringOpt("building_name")
        ),
        contact = Contact(
          email = resultSet.string("email"),
          phone = resultSet.string("phone")
        )
      ),
      config = CustomerConfig(
        loginName = resultSet.string("login_name"),
        password = resultSet.string("password"),
        favoriteCategoryId = resultSet.stringOpt("favorite_category_id").
          map(e => CategoryId(UUID.fromString(e)))
      )
    )

  def resolveByLoginName(loginName: String)(implicit ctx: EntityIOContext): Try[Customer] = Try {
    implicit val dbSession = getDBSession(ctx)
    sql"select * from $table where login_name = ?".bind(loginName).map(convertResultSetToEntity).
      single().apply().getOrElse(throw EntityNotFoundException(s"loginName = $loginName"))
  }

}
