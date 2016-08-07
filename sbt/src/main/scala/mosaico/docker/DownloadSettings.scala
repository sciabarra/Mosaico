package mosaico.docker

import akka.util.ByteString
import sbt._, Keys._
import java.net._, java.io._

import akka.http.scaladsl._
import akka.stream.scaladsl._
import akka.http.scaladsl.model._

import scala.concurrent.{Promise, Await}
import scala.util.{Failure, Success}

trait DownloadSettings {
  this: AutoPlugin =>

  import mosaico.common.AkkaCommon._
  import MosaicoDockerKeys._

  def fileFromUrl(url: String): Option[File] = {
    //println(s"fileFromUrl ${url}")
    val u = new URL(url)
    val f = new File(u.getFile)
    val name = f.getName
    if (name.length > 0)
      Some(new File(name))
    else None
  }

  private var total: Double = 0
  private var counter: Long = 0
  private var nextCheck: Long = 0

  def initDownload(len: Long): Unit = {
    counter = 0
    total = len
    nextCheck = System.currentTimeMillis() - CHECK_INTERVAL
  }

  def progressDownload(bs: ByteString): ByteString = {
    counter += bs.size
    val now = System.currentTimeMillis()
    if (now > nextCheck) {
      nextCheck += CHECK_INTERVAL
      print(if (total >= 0) {
        "%02.2f%%".format(counter / total)
      } else "")
      println(s" ${counter.toLong / MEGA}/${total.toLong / MEGA}")
    }
    bs
  }

  def downloadUrl(url: URL, file: File): Option[File] = {

    //println(s"downloadUrl: ${url} ${file}")

    val req = HttpRequest(uri = url.toString)
    val res = waitFor(Http().singleRequest(req))

    println(res)

    val ent = res.entity
    val len = ent.contentLengthOption.getOrElse(-1l)
    initDownload(len)

    val toDownload = if (file.exists()) {
      if (len > 0 && file.length() == len) {
        println("file already downloaded")
        false
      } else {
        file.delete()
        println(s"removed ${file}")
        true
      }
    } else true

    if (toDownload) {
      val out = new FileOutputStream(file).getChannel
      waitFor(ent.withSizeLimit(SIZE_LIMIT).
        dataBytes.
        map(progressDownload).
        runForeach(bs => out.write(bs.asByteBuffer))
      )
    }
    Some(file)
  }

  def usage = {
    println("usage: download <url> [file]")
    None
  }

  val downloadTask = download := {
    val args: Seq[String] = Def.spaceDelimited("<arg>").parsed
    args.length match {
      case 0 =>
        usage
      case 1 =>
        downloadUrl(new URL(args(0)),
          fileFromUrl(args(0)).getOrElse(File.createTempFile("download", ".tmp")))
      case 2 =>
        downloadUrl(new URL(args(0)), new File(args(1)))
      case _ => usage
    }
  }

  val downloadSettings = Seq(downloadTask)
}
