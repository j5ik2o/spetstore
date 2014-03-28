package com.github.j5ik2o.spetstore.domain.lifecycle.item

import com.github.j5ik2o.spetstore.domain.infrastructure.support.{EntityIOContext, RepositoryOnJDBC}
import java.util.UUID

import scalikejdbc._, SQLInterpolation._
import com.github.j5ik2o.spetstore.domain.model.item.{CategoryId, ItemType, ItemTypeId}
import scala.util.Try
import com.github.j5ik2o.spetstore.domain.infrastructure.db.CRUDMapper
import com.github.j5ik2o.spetstore.infrastructure.db.ItemTypeRecord


private[item]
class ItemTypeRepositoryOnJDBC
  extends RepositoryOnJDBC[ItemTypeId, ItemType] with ItemTypeRepository {

  override type T = ItemTypeRecord

  override protected val mapper = ItemTypeRecord

  override def deleteByIdentifier(identifier: ItemTypeId)(implicit ctx: ItemTypeRepositoryOnJDBC#Ctx): Try[(ItemTypeRepositoryOnJDBC#This, ItemType)] = ???

  override def storeEntity(entity: ItemType)(implicit ctx: ItemTypeRepositoryOnJDBC#Ctx): Try[(ItemTypeRepositoryOnJDBC#This, ItemType)] = ???

  override def resolveEntities(offset: Int, limit: Int)(implicit ctx: EntityIOContext): Try[Seq[ItemType]] = ???

  override def resolveEntity(identifier: ItemTypeId)(implicit ctx: ItemTypeRepositoryOnJDBC#Ctx): Try[ItemType] = ???
}
