package spetstore.interface.generator.jdbc

import monix.eval.Task
import slick.jdbc.JdbcProfile
import spetstore.domain.model.item.ItemId

class ItemIdGeneratorOnJDBC(override val profile: JdbcProfile, override val db: JdbcProfile#Backend#Database)
    extends AbstractIdGeneratorOnJDBC[ItemId](profile, db, "item_id_sequence_number") {
  override def generateId(): Task[ItemId] = internalGenerateId().map(ItemId)
}
