package mosaico.docker

import sbt._, Keys._
import mosaico.common.Download
import java.net._


trait DownloadSettings extends Download {
  this: AutoPlugin =>

  import MosaicoDockerKeys._

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
