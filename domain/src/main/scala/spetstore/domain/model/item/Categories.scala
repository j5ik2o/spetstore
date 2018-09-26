package spetstore.domain.model.item

import _root_.io.circe
import io.circe.parser
import io.circe.syntax._

case class Categories(breachEncapsulationOfValues: Set[String]) {
  def asString: String = breachEncapsulationOfValues.asJson.noSpaces
}

object Categories {
  def parse(text: String): Either[circe.Error, Categories] = {
    parser.parse(text).flatMap(_.as[Set[String]]).map(Categories.apply)
  }
}
