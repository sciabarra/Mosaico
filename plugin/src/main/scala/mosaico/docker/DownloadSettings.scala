package mosaico.docker

import akka.http.scaladsl.model.headers.RawHeader
import akka.util.ByteString
import sbt._, Keys._
import java.net._, java.io._

import akka.http.scaladsl._
import akka.stream.scaladsl._
import akka.http.scaladsl.model._

import scala.collection.immutable
import scala.concurrent.{Promise, Await}
import scala.util.{Failure, Success}

trait DownloadSettings {
  this: AutoPlugin =>

  import mosaico.common.AkkaCommon._
  import MosaicoDockerKeys._

  def fileFromUrl(base: File, url: String): File = {
    //println(s"fileFromUrl ${url}")
    val u = new URL(url)
    val f = new File(u.getFile)
    val name = f.getName
    if (name.length > 0)
      new File(base, name)
    else File.createTempFile("download", ".tmp", base)
  }

  def fileFromString(base: File, filename: String, url: String): File = {
    if (filename.equals("-"))
      fileFromUrl(base, url)
    else
      new File(base, filename)
  }

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

  def downloadUrl(url: URL, file: File, header: Option[String] = None): Option[File] = {

    //println(s"downloadUrl: ${url} ${file} ${header}")

    val incomplete = new File(file.getAbsolutePath + ".dld")

    val headerSeq = header.map {
      s => val a: Array[String] = s.split(":")
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

    if (toDownload) {

      val req = HttpRequest(uri = url.toString, headers = headerSeq)

      println(req)
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
    }
    Some(file)
  }

  def usage = {
    println("usage: download <url> [file] [header...]")
    None
  }

  val downloadTask = download := {
    val args: Seq[String] = Def.spaceDelimited("<arg>").parsed
    val base = baseDirectory.value
    args.length match {
      case 0 =>
        usage
      case 1 =>
        downloadUrl(new URL(args(0)), fileFromUrl(base, args(0)))
      case 2 =>
        downloadUrl(new URL(args(0)), fileFromString(base, args(1), args(0)))
      case _ =>
        downloadUrl(new URL(args(0)), fileFromString(base, args(1), args(0)), Some(args.tail.tail.mkString(" ")))
    }
  }

  val downloadSettings = Seq(downloadTask)
}
