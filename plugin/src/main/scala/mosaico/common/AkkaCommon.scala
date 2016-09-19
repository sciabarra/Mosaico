package mosaico.common

import java.security.{KeyManagementException, NoSuchAlgorithmException, SecureRandom}
import java.security.cert.X509Certificate
import javax.net.ssl._

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.concurrent._
import scala.concurrent.duration._


/**
  * Akka common object: initialize an actor system
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

  def waitFor[T](p: Awaitable[T]) = Await.result[T](p, TIME_LIMIT)

  private def disableSslVerification {
    try {
      val trustAllCerts: Array[TrustManager] = Array[TrustManager](new X509TrustManager() {
        def getAcceptedIssuers: Array[X509Certificate] = {
          return null
        }

        def checkClientTrusted(certs: Array[X509Certificate], authType: String) {
        }

        def checkServerTrusted(certs: Array[X509Certificate], authType: String) {
        }
      })
      val sc: SSLContext = SSLContext.getInstance("SSL")
      sc.init(null, trustAllCerts, new SecureRandom)
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory)
      val allHostsValid: HostnameVerifier = new HostnameVerifier() {
        def verify(hostname: String, session: SSLSession): Boolean = {
          return true
        }
      }
      HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid)
    }
    catch {
      case e: NoSuchAlgorithmException => {
        e.printStackTrace
      }
      case e: KeyManagementException => {
        e.printStackTrace
      }
    }
  }

  try {
    disableSslVerification
  }

}
