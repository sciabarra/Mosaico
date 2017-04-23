package mosaico.docker

import java.io.{File, FileInputStream, FileOutputStream}
import java.util.zip.{ZipEntry, ZipInputStream}
import java.net._

import mosaico.common.{Download, MiscUtils}
import mosaico.config.MosaicoConfigPlugin
import sbt.{file, _}
import Keys._

/**
  * Download  settings see documentation for details
  */
trait DownloadSettings
  extends Download
    with MiscUtils {
  this: AutoPlugin =>


  trait DownloadKeys {
    lazy val download = inputKey[Option[File]]("download")
  }

  import MosaicoConfigPlugin.autoImport._
  import MosaicoDockerPlugin.autoImport._

  def usage = {
    println("usage: download {url} [{file}] [{header}...]")
    None
  }

  val downloadTask = download := {

    val args: Seq[String] = replaceAtWithMap(Def.spaceDelimited("<arg>").parsed, prp.value)
    val base = baseDirectory.value

    args.length match {
      case 0 =>
        usage
      case 1 =>
        downloadUrl(new URL(args(0)), fileFromUrl(base, args(0)))
      case 2 =>
        downloadUrl(new URL(args(0)), fileFromStringOrUrl(base, args(1), args(0)))
      case _ =>
        downloadUrl(new URL(args(0)), fileFromStringOrUrl(base, args(1), args(0)), Some(args.tail.tail.mkString(" ")))
    }
  }

  val downloadSettings = Seq(downloadTask)
}
