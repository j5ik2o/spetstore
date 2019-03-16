package spetstore

import java.net.InetSocketAddress

import akka.actor.ActorSystem
import cats.data.NonEmptyList
import com.github.j5ik2o.reactive.redis.{ PeerConfig, RedisConnection, RedisConnectionPool }
import monix.eval.Task
import monix.execution.Scheduler
import org.hashids.Hashids
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import spetstore.domain.model.UserAccountId
import spetstore.domain.model.item.ItemId
import spetstore.interface.api.controller.{ ItemController, UserAccountController }
import spetstore.interface.api.{ ApiServer, Routes, SwaggerDocService }
import spetstore.interface.generator.{ ItemIdGeneratorOnJDBC, UserAccountIdGeneratorOnJDBC }
import spetstore.interface.repository.{ CartRepositoryOnRedis, ItemRepositoryOnJDBC, UserAccountRepositoryOnJDBC }
import spetstore.useCase.port.generator.IdGenerator
import spetstore.useCase.port.repository.CartRepository.OnRedis
import spetstore.useCase.port.repository.{ CartRepository, ItemRepository, UserAccountRepository }
import wvlet.airframe._

import scala.concurrent.duration._

package object interface {

  private def connectionPool(sizePerPeer: Int,
                             host: String,
                             port: Int)(implicit system: ActorSystem, scheduler: Scheduler): RedisConnectionPool[Task] =
    RedisConnectionPool.ofMultipleRoundRobin(
      sizePerPeer,
      peerConfigs = NonEmptyList.of(PeerConfig(new InetSocketAddress(host, port))),
      newConnection = RedisConnection.apply
    )

  def createInterfaceDesign(host: String,
                            port: Int,
                            hashidsSalt: String,
                            apiClasses: Set[Class[_]],
                            dbConfig: DatabaseConfig[JdbcProfile],
                            redisHost: String,
                            redisPort: Int)(implicit system: ActorSystem): Design =
    createInterfaceDesign(host, port, hashidsSalt, apiClasses, dbConfig.profile, dbConfig.db, redisHost, redisPort)

  def createInterfaceDesign(host: String,
                            port: Int,
                            hashidsSalt: String,
                            apiClasses: Set[Class[_]],
                            profile: JdbcProfile,
                            db: JdbcProfile#Backend#Database,
                            redisHost: String,
                            redisPort: Int)(
      implicit system: ActorSystem
  ): Design = {

    val redisExpireDuration = system.settings.config.getDuration("spetstore.interface.storage.redis.expire")
    implicit val scheduler  = Scheduler(system.dispatcher)
    newDesign
      .bind[RedisConnectionPool[Task]].toInstance(connectionPool(2, redisHost, redisPort))
      .bind[Hashids].toInstance(new Hashids(hashidsSalt))
      .bind[JdbcProfile].toInstance(profile)
      .bind[JdbcProfile#Backend#Database].toInstance(db)
      .bind[Routes].toSingleton
      .bind[SwaggerDocService].toInstance(new SwaggerDocService(host, port, apiClasses))
      .bind[ApiServer].toSingleton
      .bind[UserAccountRepository[Task]].to[UserAccountRepositoryOnJDBC]
      .bind[IdGenerator[UserAccountId]].to[UserAccountIdGeneratorOnJDBC]
      .bind[UserAccountController].toSingleton
      .bind[ItemRepository[Task]].to[ItemRepositoryOnJDBC]
      .bind[IdGenerator[ItemId]].to[ItemIdGeneratorOnJDBC]
      .bind[ItemController].toSingleton
      .bind[CartRepository[OnRedis]].toInstance(new CartRepositoryOnRedis(redisExpireDuration.toMillis millis))
      .add(useCase.createUseCaseDesign)
  }

}
