package mosaico.samples

import java.io.File

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

  def dump(bs: ByteString): ByteString = {
    println(bs.size)
    bs
  }

  def download(url: String): Unit = {
    val request = HttpRequest(uri = url.toString)
    val response = Http().singleRequest(request)
    println(response)
    val res = waitFor(response)
    println(res)
    println(res.entity.isChunked())
    val str = res.entity.withSizeLimit(SIZE_LIMIT).dataBytes
    waitFor(str.runForeach(bs =>
      println(bs.size)
    ))
    val out = FileIO.toFile(new File("out"))
    waitFor(str.map(dump).runWith(out))
  }

  download("http://d3kbcqa49mib13.cloudfront.net/spark-2.0.0-bin-hadoop2.7.tgz")
}
