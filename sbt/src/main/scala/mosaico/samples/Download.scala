package mosaico.samples

import java.io.{FileOutputStream, File}

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.stream.scaladsl.FileIO
import akka.util.ByteString
import mosaico.common.AkkaCommon

/**
  * Created by msciab on 07/08/16.
  */
object Download extends App {

  import AkkaCommon._

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
        "%02.2f%%".format(counter / total)
      } else "")
      println(s" ${counter.toLong/MEGA }m/${total.toLong/MEGA}m")
    }
    bs
  }

  def download(url: String): Unit = {
    val request = HttpRequest(uri = url.toString)
    val response = Http().singleRequest(request)
    println(response)
    val res = waitFor(response)
    println(res)
    println(res.entity.isChunked())
    val ent = res.entity
    initDownload(ent.contentLengthOption.getOrElse(-1L))
    val str = ent.withSizeLimit(SIZE_LIMIT).dataBytes
    val out = new FileOutputStream("out").getChannel
    waitFor(str.map(progressDownload).runForeach(bs =>
      out.write(bs.asByteBuffer)))
  }

  download("http://d3kbcqa49mib13.cloudfront.net/spark-2.0.0-bin-hadoop2.7.tgz")
}
