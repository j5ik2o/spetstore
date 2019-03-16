package spetstore.interface.dao

import com.github.j5ik2o.dddbase.slick.SlickDaoSupport

trait ItemComponent extends SlickDaoSupport {

  import profile.api._

  case class ItemRecord(
      id: Long,
      status: String,
      name: String,
      description: Option[String],
      categories: String,
      price: Long,
      createdAt: java.time.ZonedDateTime,
      updatedAt: Option[java.time.ZonedDateTime]
  ) extends SoftDeletableRecord

  case class Items(tag: Tag) extends TableBase[ItemRecord](tag, "item") with SoftDeletableTableSupport[ItemRecord] {
<<<<<<< HEAD
    def id: Rep[Long]                                   = column[Long]("id")
=======
    // def id = column[Long]("id", O.PrimaryKey)
>>>>>>> 76d011b54580d55b7b01d08dfbd2406b6d02f826
    def status: Rep[String]                             = column[String]("status")
    def name: Rep[String]                               = column[String]("name")
    def description: Rep[Option[String]]                = column[Option[String]]("description")
    def categories: Rep[String]                         = column[String]("categories")
    def price: Rep[Long]                                = column[Long]("price")
    def createdAt: Rep[java.time.ZonedDateTime]         = column[java.time.ZonedDateTime]("created_at")
    def updatedAt: Rep[Option[java.time.ZonedDateTime]] = column[Option[java.time.ZonedDateTime]]("updated_at")
    override def * =
      (id, status, name, description, categories, price, createdAt, updatedAt) <> (ItemRecord.tupled, ItemRecord.unapply)
  }

  object ItemDao extends TableQuery(Items)

}
