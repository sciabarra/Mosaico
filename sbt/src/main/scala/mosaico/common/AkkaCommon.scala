package mosaico.common

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.concurrent._
import scala.concurrent.duration._


/**
  * Created by msciab on 04/08/16.
  */
object AkkaCommon {

  final val NAME = "mosaico"
  final val KILO = 1000
  final val MEGA = KILO * 1000
  final val GIGA = MEGA * 1000
  final val SIZE_LIMIT = 100 * GIGA
  final val TIME_LIMIT = 1.hour
  final val CHECK_INTERVAL = 5 * 1000

  val classLoader = getClass.getClassLoader
  val config = ConfigFactory.load(classLoader)
    .getConfig(NAME)
    .withFallback(ConfigFactory.defaultReference(classLoader))

  implicit val system = ActorSystem(NAME, config, classLoader)

  implicit val materializer = ActorMaterializer()

  implicit val ec = ExecutionContext.fromExecutor(system.dispatcher)

  //def waitFor(f: Future) = Await.result(f, TIME_LIMIT)

  def waitFor[T](p: Awaitable[T]) = Await.result[T](p, TIME_LIMIT)

}
