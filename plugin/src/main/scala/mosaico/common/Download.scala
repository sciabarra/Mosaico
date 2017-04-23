package mosaico.common

import java.io.{File, FileOutputStream}
import java.net.URL

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.model.headers.RawHeader
import akka.util.ByteString
import sbt.URL

import scala.collection.immutable

/**
  * Download urls with Akka
  */
trait Download extends FileUtils {

  import mosaico.common.AkkaCommon._

  private var total: Double = 0
  private var counter: Long = 0
  private var nextCheck: Long = 0

  def initDownload(len: Long): Unit = {
    counter = 0
    total = len
    nextCheck = System.currentTimeMillis()
  }

  def progressDownload(bs: ByteString): ByteString = {
    counter += bs.size
    val now = System.currentTimeMillis()
    if (now > nextCheck) {
      nextCheck += CHECK_INTERVAL
      print(if (total >= 0) {
        "%02.0f%%".format(counter / total * 100)
      } else "")
      println(s" ${counter.toLong / MEGA}/${total.toLong / MEGA}")
    }
    bs
  }


  def downloadUrlHttps(url: URL, file: File) = {
    println(s"<<< ${url}")
    sbt.IO.download(url, file)
    println(s">>> ${file}")
    Some(file)
  }

  /**
    * Download the url to the file, adds optionally the specified header
    *
    * @param url
    * @param file
    * @param header
    * @return
    */
  def downloadUrl(url: URL, file: File, header: Option[String] = None): Option[File] = {

    //println(s"downloadUrl: ${url} ${file} ${header}")

    val incomplete = new File(file.getAbsolutePath + ".dld")

    val headerSeq = header.map {
      s =>
        val a: Array[String] = s.split(":")
        immutable.Seq(new RawHeader(a.head, a.tail.mkString(":")))
    }.getOrElse(Nil)


    val toDownload = if (file.exists()) {
      if (!incomplete.exists()) {
        println("file already downloaded")
        false
      } else {
        file.delete()
        incomplete.delete()
        println(s"removed ${file}")
        true
      }
    } else true

    if (toDownload && url.getProtocol == "https") {
      // workaround for https until sbt upgrade to 2.11/2.12
      println(s"<<< ${url}")
      sbt.IO.download(url, file)
      println(s">>> ${file}")
    } else if (toDownload && url.getProtocol == "http") {

      val req = HttpRequest(uri = url.toString, headers = headerSeq)

      println(s"<<< ${url}")
      var res = waitFor(Http().singleRequest(req))
      while (res.getHeader("Location").nonEmpty) {
        val redir = res.getHeader("Location").get.value
        println(redir)
        res = waitFor(Http().singleRequest(
          HttpRequest(uri = redir, headers = headerSeq)))
      }
      println(res)

      val ent = res.entity
      val len = ent.contentLengthOption.getOrElse(-1l)
      initDownload(len)

      incomplete.createNewFile()
      val out = new FileOutputStream(file).getChannel
      waitFor(ent.withSizeLimit(SIZE_LIMIT).
        dataBytes.
        map(progressDownload).
        runForeach(bs => out.write(bs.asByteBuffer))
      )
      if (res.status.intValue == 200)
        incomplete.delete()
      println(s">>> ${file}")
    }
    Some(file)
  }


}
