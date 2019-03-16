package spetstore.interface.dao

import com.github.j5ik2o.dddbase.slick.SlickDaoSupport

trait UserAccountComponent extends SlickDaoSupport {

  import profile.api._

  case class UserAccountRecord(
      id: Long,
      status: String,
      emailAddress: String,
      firstName: String,
      lastName: String,
      hashedPassword: String,
      createdAt: java.time.ZonedDateTime,
      updatedAt: Option[java.time.ZonedDateTime]
  ) extends SoftDeletableRecord

  case class UserAccounts(tag: Tag)
      extends TableBase[UserAccountRecord](tag, "user_account")
      with SoftDeletableTableSupport[UserAccountRecord] {
    // def id = column[Long]("id", O.PrimaryKey)
    def status: Rep[String]                             = column[String]("status")
    def emailAddress: Rep[String]                       = column[String]("email_address")
    def firstName: Rep[String]                          = column[String]("first_name")
    def lastName: Rep[String]                           = column[String]("last_name")
    def hashedPassword: Rep[String]                     = column[String]("hashed_password")
    def createdAt: Rep[java.time.ZonedDateTime]         = column[java.time.ZonedDateTime]("created_at")
    def updatedAt: Rep[Option[java.time.ZonedDateTime]] = column[Option[java.time.ZonedDateTime]]("updated_at")
    override def * =
      (id, status, emailAddress, firstName, lastName, hashedPassword, createdAt, updatedAt) <> (UserAccountRecord.tupled, UserAccountRecord.unapply)
  }

  object UserAccountDao extends TableQuery(UserAccounts)

}
