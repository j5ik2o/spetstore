package com.github.j5ik2o.spetstore.domain.account

import com.github.j5ik2o.spetstore.domain.address.{Contact, Pref, ZipCode, PostalAddress}
import com.github.j5ik2o.spetstore.domain.item.CategoryId
import com.github.j5ik2o.spetstore.infrastructure.support.{EntityNotFoundException, EntityIOContext, RepositoryOnJDBC}
import java.util.UUID
import scalikejdbc.WrappedResultSet
import scala.util.Try

private[account]
class AccountRepositoryOnJDBC
  extends RepositoryOnJDBC[AccountId, Account] with AccountRepository {

  override val tableName = "account"

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
    "password",
    "favorite_category_id"
  )

  protected def convertEntityToValues(entity: Account): Seq[Any] = Seq(
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
    entity.config.password,
    entity.config.favoriteCategoryId.map(_.value.toString)
  )

  protected def convertResultSetToEntity(resultSet: WrappedResultSet): Account =
    Account(
      id = AccountId(UUID.fromString(resultSet.string("id"))),
      name = resultSet.string("name"),
      status = AccountStatus(resultSet.int("status")),
      profile = AccountProfile(
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
      config = AccountConfig(
        password = resultSet.string("password"),
        favoriteCategoryId = resultSet.stringOpt("favorite_category_id").
          map(e => CategoryId(UUID.fromString(e)))
      )
    )

  def resolveByName(name: String)(implicit ctx: EntityIOContext): Try[Account] = Try {
    implicit val dbSession = getDBSession(ctx)
    sql"select * from $table where name = ?".bind(name).map(convertResultSetToEntity).
      single().apply().getOrElse(throw EntityNotFoundException(s"name = $name"))
  }

}
