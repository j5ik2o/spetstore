package spetstore.interface.dao

import java.time.{ Instant, ZoneId, ZonedDateTime }

import akka.actor.ActorSystem
import cats.data.{ NonEmptyList, ReaderT }
import cats.implicits._
import com.github.j5ik2o.dddbase.redis.RedisDaoSupport
import com.github.j5ik2o.reactive.redis.{ ReaderTTask, RedisConnection, Result }
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import io.circe.{ Decoder, Encoder }
import monix.eval.Task
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

trait CartComponent extends RedisDaoSupport {

  implicit val zonedDateTimeEncoder: Encoder[ZonedDateTime] = Encoder[Long].contramap(_.toInstant.toEpochMilli)
  implicit val zonedDateTimeDecoder: Decoder[ZonedDateTime] = Decoder[Long].map { ts =>
    ZonedDateTime.ofInstant(Instant.ofEpochMilli(ts), ZoneId.systemDefault())
  }

  case class CartItemRecord(id: Long, no: Int, itemId: Long, quantity: Long, inStock: Boolean)

  case class CartRecord(id: String,
                        status: String,
                        userAccountId: Long,
                        cartItems: Seq[CartItemRecord],
                        createdAt: java.time.ZonedDateTime,
                        updatedAt: java.time.ZonedDateTime)
      extends SoftDeletableRecord {
    override type This = CartRecord

    override def withStatus(value: String): CartRecord =
      copy(status = value)
  }

  case class CartDao()(implicit val system: ActorSystem)
      extends Dao[ReaderT[Task, RedisConnection, ?], CartRecord]
      with DaoSoftDeletable[ReaderT[Task, RedisConnection, ?], CartRecord] {
    val logger = LoggerFactory.getLogger(getClass)

    val DELETED = "deleted"

    private def internalSet(record: CartRecord, expire: Duration): ReaderT[Task, RedisConnection, Result[Unit]] = {
      expire match {
        case e if e.isFinite() && e.lt(1 seconds) =>
          redisClient.pSetEx(record.id, FiniteDuration(expire._1, expire._2), toJsonString(record))
        case e if e.isFinite() && !e.lt(1 seconds) =>
          redisClient.setEx(record.id, FiniteDuration(expire._1, expire._2), toJsonString(record))
        case e if !e.isFinite() =>
          redisClient.set(record.id, toJsonString(record))
      }
    }

    override def set(record: CartRecord, expire: Duration): ReaderT[Task, RedisConnection, Long] = ReaderT { con =>
      internalSet(record, expire).run(con).map(_ => 1L)
    }

    override def setMulti(records: Seq[CartRecord], expire: Duration): ReaderT[Task, RedisConnection, Long] = ReaderT {
      con =>
        Task
          .traverse(records) { record =>
            set(record, expire).run(con)
          }
          .map(_.count(_ > 0))
    }

    private def internalGet(id: String): ReaderT[Task, RedisConnection, Option[CartRecord]] =
      redisClient
        .get(id)
        .map {
          _.value.flatMap { v =>
            logger.debug(s"internalGet: $v")
            val r = parse(v).leftMap(error => new Exception(error.message)).flatMap { json =>
              logger.debug(s"internalGet: parse: $json")
              json.as[CartRecord].leftMap(error => new Exception(error.message)).map { v =>
                if (v.status == DELETED)
                  None
                else
                  Some(v)
              }
            }
            r match {
              case Right(v) =>
                v
              case Left(ex) =>
                throw ex
            }
          }
        }

    override def get(id: String): ReaderT[Task, RedisConnection, Option[CartRecord]] = ReaderT { con =>
      internalGet(id).run(con)
    }

    override def getMulti(ids: Seq[String]): ReaderT[Task, RedisConnection, Seq[CartRecord]] = ReaderT { con =>
      Task
        .traverse(ids) { id =>
          get(id).run(con)
        }
        .map(_.foldLeft(Seq.empty[CartRecord]) {
          case (result, e) =>
            result ++ e.map(Seq(_)).getOrElse(Seq.empty)
        })
    }

    override def delete(id: String): ReaderT[Task, RedisConnection, Long] = ReaderT { con =>
      redisClient.del(NonEmptyList.of(id)).run(con).map { _.value.toLong }
    }

    override def deleteMulti(ids: Seq[String]): ReaderT[Task, RedisConnection, Long] = ReaderT { con =>
      Task
        .traverse(ids) { id =>
          delete(id).run(con)
        }
        .map(_.count(_ > 0))
    }

    override def softDelete(id: String): ReaderT[Task, RedisConnection, Long] = get(id).flatMap {
      case Some(v) =>
        set(v.withStatus(DELETED), Duration.Inf)
      case None =>
        ReaderTTask.pure(0L)
    }

    override def softDeleteMulti(ids: Seq[String]): ReaderT[Task, RedisConnection, Long] = getMulti(ids).flatMap {
      values =>
        setMulti(values.map(_.withStatus(DELETED)), Duration.Inf)
    }

    private def toJsonString(record: CartRecord): String = {
      record.asJson.noSpaces
    }
  }

}
