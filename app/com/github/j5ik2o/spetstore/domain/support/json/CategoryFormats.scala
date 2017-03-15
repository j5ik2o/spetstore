package com.github.j5ik2o.spetstore.domain.support.json

import com.github.j5ik2o.spetstore.domain.model.basic.StatusType
import com.github.j5ik2o.spetstore.domain.model.item.{ CategoryId, Category }
import org.json4s.DefaultReaders._
import org.json4s._

object CategoryFormats {

  implicit object CategoryFormat extends Reader[Category] with Writer[Category] {
    override def read(value: JValue): Category = Category(
      id = CategoryId((value \ "id").as[Long]),
      status = StatusType((value \ "status").as[Int]),
      name = (value \ "name").as[String],
      description = (value \ "description").getAs[String],
      version = (value \ "version").as[Option[String]].map(_.toLong)
    )

    override def write(obj: Category): JValue = JObject(
      JField("id", JInt(obj.id.value)),
      JField("status", JInt(obj.status.id)),
      JField("name", JString(obj.name)),
      JField("description", obj.description.map(JString).getOrElse(JNull)),
      JField("version", obj.version.map(e => JString(e.toString)).getOrElse(JNull))
    )
  }

}
