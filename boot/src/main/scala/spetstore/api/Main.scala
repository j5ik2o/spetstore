package spetstore.api

import akka.actor.ActorSystem
import akka.http.scaladsl.settings.ServerSettings
import monix.eval.Task
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import spetstore.domain.model.item.ItemId
import spetstore.interface.api.controller.ItemController
import spetstore.interface.api.{ ApiServer, Routes, SwaggerDocService }
import spetstore.interface.generator.IdGenerator
import spetstore.interface.generator.jdbc.ItemIdGeneratorOnJDBC
import spetstore.interface.repository.{ ItemRepository, ItemRepositoryBySlick }
import wvlet.airframe._

object Main {

  def main(args: Array[String]): Unit = {
    val parser = new scopt.OptionParser[AppConfig]("spetstore") {
      opt[String]('h', "host").action((x, c) => c.copy(host = x)).text("host")
      opt[Int]('p', "port").action((x, c) => c.copy(port = x)).text("port")
    }
    val system = ActorSystem("akdme")
    val dbConfig: DatabaseConfig[JdbcProfile] =
      DatabaseConfig.forConfig[JdbcProfile](path = "spetstore.interface.storage.jdbc", system.settings.config)

    parser.parse(args, AppConfig()) match {
      case Some(config) =>
        val design = newDesign
          .bind[ActorSystem].toInstance(system)
          .bind[JdbcProfile#Backend#Database].toInstance(dbConfig.db)
          .bind[Routes].toSingleton
          .bind[SwaggerDocService].toInstance(
            new SwaggerDocService(config.host, config.port, Set(classOf[ItemController]))
          )
          .bind[ApiServer].toSingleton
          .bind[ItemRepository[Task]].to[ItemRepositoryBySlick]
          .bind[IdGenerator[ItemId]].to[ItemIdGeneratorOnJDBC]
        design.withSession { session =>
          val system = session.build[ActorSystem]
          session.build[ApiServer].start(config.host, config.port, settings = ServerSettings(system))
        }
      case None =>
        println(parser.usage)
    }
  }
}
