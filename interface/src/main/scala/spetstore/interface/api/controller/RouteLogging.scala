package spetstore.interface.api.controller

import java.io.{ PrintWriter, StringWriter }

import akka.event.Logging.LogLevel
import akka.event.{ Logging, LoggingAdapter }
import akka.http.scaladsl.common.StrictForm
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{ Authorization, BasicHttpCredentials }
import akka.http.scaladsl.server.directives.{ DebuggingDirectives, LoggingMagnet }
import akka.http.scaladsl.server.{ Directive0, RouteResult }
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.util.FastFuture._
import akka.stream.Materializer
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.{ Json, JsonObject }

import scala.annotation.tailrec
import scala.collection.immutable
import scala.concurrent.duration._
import scala.concurrent.{ ExecutionContext, Future }
import scala.util.{ Failure, Success }

trait RequestLogging {
  def handler(loggingAdapter: LoggingAdapter)(request: HttpRequest): Unit
}

trait RequestResultLogging {
  def handler(loggingAdapter: LoggingAdapter)(request: HttpRequest): RouteResult => Unit
}

trait LoggingSupport extends FailFastCirceSupport {

  final val MASK_KEYS = Seq()

  final val BODY_PARSE_TIMEOUT = 5 seconds

  def logging(loggingAdapter: LoggingAdapter, logLevel: LogLevel, request: HttpRequest, fallback: Boolean)(
      f: (HttpRequest, Boolean) => Future[String]
  )(implicit ec: ExecutionContext): Unit = {
    f(request, fallback).onComplete {
      case Success(req) =>
        loggingAdapter.log(logLevel, s"$req /" + (if (fallback) " (fallback)" else ""))
      case Failure(t) =>
        if (!fallback) {
          logging(loggingAdapter, logLevel, request, fallback = true)(f)
        } else {
          val sw = new StringWriter()
          val pw = new PrintWriter(sw)
          t.printStackTrace(pw)
          loggingAdapter.warning(s"occurred logging warning: ${sw.toString}")
        }
    }
  }

  def logging(
      loggingAdapter: LoggingAdapter,
      logLevel: LogLevel,
      request: HttpRequest,
      response: HttpResponse,
      fallback: Boolean
  )(f: (HttpRequest, HttpResponse, Boolean) => Future[(String, String)])(implicit ec: ExecutionContext): Unit = {
    f(request, response, fallback).onComplete {
      case Success((req, res)) =>
        loggingAdapter.log(logLevel, s"$req / $res" + (if (fallback) " (fallback)" else ""))
      case Failure(t) =>
        if (!fallback) {
          logging(loggingAdapter, logLevel, request, response, fallback = true)(f)
        } else {
          val sw = new StringWriter()
          val pw = new PrintWriter(sw)
          t.printStackTrace(pw)
          loggingAdapter.warning(s"occurred logging warning: ${sw.toString}")
        }
    }
  }

  class DefaultRequestLogging(logLevel: Logging.LogLevel = Logging.InfoLevel)(implicit ec: ExecutionContext,
                                                                              mat: Materializer)
      extends RequestLogging {

    override def handler(loggingAdapter: LoggingAdapter)(request: HttpRequest): Unit =
      logging(loggingAdapter, logLevel, request, fallback = false)(formatRequestToString)

  }

  class DefaultRequestResultLogging(logLevel: Logging.LogLevel = Logging.InfoLevel)(implicit ec: ExecutionContext,
                                                                                    mat: Materializer)
      extends RequestResultLogging {
    override def handler(loggingAdapter: LoggingAdapter)(request: HttpRequest): (RouteResult) => Unit = {
      case RouteResult.Complete(response) =>
        logging(loggingAdapter, logLevel, request, response, fallback = false) { (request, response, fallback) =>
          for {
            req <- formatRequestToString(request, fallback)
            res <- formatResponseToString(response, fallback)
          } yield (req, res)
        }
      case _ =>
        logging(loggingAdapter, logLevel, request, fallback = false)(formatRequestToString)
    }
  }

  private def formatRequestToString(
      request: HttpRequest,
      fallback: Boolean = false
  )(implicit ec: ExecutionContext, mat: Materializer): Future[String] = {
    val protocol = request.protocol.value
    val method   = request.method.name()
    val path     = request.uri.toString()
    val headers = request.headers
      .map {
        case Authorization(BasicHttpCredentials(username, _)) => s"authorization:Basic username=$username"
        case Authorization(_)                                 => "authorization:***"
        case h                                                => s"'${h.lowercaseName()}':'${h.value()}'"
      }
      .mkString("[", ",", "]")

    (request.entity.contentType.mediaType, fallback) match {
      case (_, true) =>
        Future.successful(s"$protocol $method $path $headers []")
      case (MediaTypes.`application/x-www-form-urlencoded`, _) if request.entity.contentLengthOption.exists(_ > 0) =>
        formFieldMultiMap(request.entity).map { formMap =>
          val formAsString = formMap
            .map { case (k, v) => s"($k=${v.mkString("[", ",", "]")})" }
            .mkString("[", ",", "]")
          s"$protocol $method $path $headers $formAsString"
        }
      case (MediaTypes.`application/json`, _) if request.entity.contentLengthOption.exists(_ > 0) =>
        jsonFieldSeq(request.entity)
          .map { jsonSeq =>
            val jsonSeqAsString = jsonSeq.map { case (k, v) => s"($k=$v)" }.mkString("{", ",", "}")
            s"$protocol $method $path $headers $jsonSeqAsString"
          }
      case _ =>
        Future.successful(s"$protocol $method $path $headers []")
    }
  }

  private def jsonFieldSeq(
      entity: HttpEntity
  )(implicit ec: ExecutionContext, mat: Materializer): Future[immutable.Seq[(String, String)]] = {
    def toKeyAndValueAsString(key: String, json: Json): (String, String) = (key, json) match {
      case (k, v) if v.isNull                            => (k, "")
      case (k, v) if v.isBoolean                         => (k, v.asBoolean.map(_.toString).get)
      case (k, v) if v.isNumber                          => (k, v.asNumber.map(_.toString).get)
      case (k, v) if v.isString && MASK_KEYS.contains(k) => (k, "***")
      case (k, v) if v.isString                          => (k, v.asString.get)
      case (k, v) if v.isObject =>
        val value = toKeyValueSeq(v.asObject)
          .map { e =>
            s"${e._1}=${e._2}"
          }
          .mkString("{", ",", "}")
        (k, value)
      case (k, v) if v.isArray =>
        val value = v.asArray
          .map { array =>
            array
              .foldLeft(Seq.empty[String]) {
                case (results, e) =>
                  results :+ toKeyAndValueAsString(k, e)._2
              }
              .mkString("[", ",", "]")
          }
          .getOrElse("[]")
        (k, value)

    }

    def toKeyValueSeq(jsonObject: Option[JsonObject]): immutable.Seq[(String, String)] = {
      jsonObject
        .map { v =>
          immutable.Seq(v.toList.map { case (key, json) => toKeyAndValueAsString(key, json) }: _*)
        }
        .getOrElse(immutable.Seq.empty[(String, String)])
    }

    entity.toStrict(BODY_PARSE_TIMEOUT).flatMap { r =>
      Unmarshal(r).to[Json].fast.map(v => toKeyValueSeq(v.asObject))
    }
  }

  private def formFieldSeq(
      entity: HttpEntity
  )(implicit ec: ExecutionContext, mat: Materializer): Future[immutable.Seq[(String, String)]] = {
    entity.toStrict(BODY_PARSE_TIMEOUT).flatMap { r =>
      Unmarshal(r).to[StrictForm].flatMap { form =>
        val fields = form.fields.collect {
          case (name, field) if name.nonEmpty =>
            Unmarshal(field).to[String].map(fieldString ⇒ (name, fieldString))
        }
        Future.sequence(fields)
      }
    }
  }

  private def formFieldMultiMap(
      entity: HttpEntity
  )(implicit ec: ExecutionContext, mat: Materializer): Future[Map[String, List[String]]] = {
    @tailrec def append(map: Map[String, List[String]],
                        fields: immutable.Seq[(String, String)]): Map[String, List[String]] = {
      if (fields.isEmpty) {
        map
      } else {
        val (key, value) = fields.head
        append(map.updated(key, value :: map.getOrElse(key, Nil)), fields.tail)
      }
    }

    formFieldSeq(entity).map(seq => append(Map.empty, seq)).map {
      _.map {
        case (k, _) if MASK_KEYS.contains(k) => (k, List("***"))
        case (k, v)                          => (k, v)
      }
    }
  }

  private def formatResponseToString(
      response: HttpResponse,
      fallback: Boolean = false
  )(implicit ec: ExecutionContext, mat: Materializer): Future[String] = {
    val protocol = response.protocol.value
    val status   = response.status.value
    val headers  = response.headers.map(h => s"'${h.lowercaseName()}':'${h.value()}'").mkString("[", ",", "]")

    (response.entity.contentType.mediaType, fallback) match {
      case (_, true) =>
        Future.successful(s"$protocol $status $headers []")
      case (MediaTypes.`application/json`, _) if response.entity.contentLengthOption.exists(_ > 0) =>
        jsonFieldSeq(response.entity)
          .map { jsonSeq =>
            val jsonSeqAsString = jsonSeq.map { case (k, v) => s"($k=$v)" }.mkString("{", ",", "}")
            s"$protocol $status $headers $jsonSeqAsString"
          }
      case _ =>
        Future.successful(s"$protocol $status $headers []")
    }
  }
}

class RouteLogging()(implicit requestLogging: RequestLogging, requestResultLogging: RequestResultLogging) {

  val httpLogRequest: Directive0 = DebuggingDirectives.logRequest(LoggingMagnet(requestLogging.handler))

  val httpLogRequestResult: Directive0 =
    DebuggingDirectives.logRequestResult(LoggingMagnet(requestResultLogging.handler))

}

object RouteLogging extends LoggingSupport {

  def apply()(implicit requestLogging: RequestLogging, requestResultLogging: RequestResultLogging): RouteLogging =
    new RouteLogging()

  def default(logLevel: Logging.LogLevel = Logging.InfoLevel)(implicit ec: ExecutionContext,
                                                              mat: Materializer): RouteLogging =
    new RouteLogging()(new DefaultRequestLogging(), new DefaultRequestResultLogging())
}
