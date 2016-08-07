package mosaico.docker

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

  def downloadUrl(url: URL, file: File): Option[File] = {
    println(s"downloadUrl: ${url} ${file}")
    val request = HttpRequest(uri = url.toString)
    val response = Http().singleRequest(request)
    println(response)

    val pr = Promise[Option[File]]
    response.onComplete {
      case Failure(err) =>
        println(s"failed request: ${err}")
        pr success None
      case Success(res) =>
        println(res.entity.getClass.toString)

        res.entity.
          withSizeLimit(SIZE_LIMIT).
          toStrict(TIME_LIMIT) onComplete {
          case Failure(err) =>
            println(s"failed stricting response ${err}")
          case Success(ent) =>
            println(ent)
            val total = ent.contentLengthOption.getOrElse(-1l).toDouble
            println(s"total ${total}")
            if (file.exists()) {
              println(s"deleting ${file}")
              file.delete()
            }

            val stream = ent.dataBytes
            val sink = FileIO.toFile(file)

            var counter = 0l
            var megaCounter = 0l
            stream.map {
              e =>
                counter += e.size
                /*
              val m = counter / MEGA
              if( m > megaCounter) {
                megaCounter = m
                print(if(total>=0) {
                  "%02.2f%".format(counter/total)
                } else "")
                println(s" ${megaCounter}m ")
              }*/
                println(counter)
                e
            }.runWith(sink).value match {
              case None =>
                pr success Some(file)
              case Some(err) =>
                println(s"failed writing: ${err}")
                pr success None
            }
        }/**/
    }
    Await.result(pr.future, TIME_LIMIT)
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
