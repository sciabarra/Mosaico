package mosaico.docker

import java.io.{FileOutputStream, FileInputStream, File}
import java.util.zip.{ZipEntry, ZipInputStream}
import java.net._
import mosaico.common.{MiscUtils, Download}
import mosaico.config.MosaicoConfigPlugin
import sbt._, Keys._

/**
  * Download  settings see documentation for details
  */

trait DownloadSettings
  extends Download
    with MiscUtils {
  this: AutoPlugin =>

  trait DownloadKeys {
    lazy val download = inputKey[Option[File]]("download")
    lazy val unzip = inputKey[Boolean]("unzip")
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

  val unzipTask = unzip := {
    val args: Seq[String] = replaceAtWithMap(Def.spaceDelimited("<arg>").parsed, prp.value)
    try {
      if (args.length == 0)
        throw new Exception("unzip files from base directory in the target folder\nusage: unzip <file.zip>")
      var outDir = baseDirectory.value  / "target"
      val zipFile = baseDirectory.value / args(0)
      var zip = new ZipInputStream(new FileInputStream(zipFile))
      val buffer: Array[Byte] = new Array[Byte](1024)
      var entry = zip.getNextEntry
      while (entry != null) {
        println(entry.getName)
        val file = outDir / entry.getName.replace('/', File.separatorChar)
        if (entry.isDirectory) {
          file.mkdirs
        } else {
          if (!file.getParentFile.exists)
            file.getParentFile.mkdirs()
          val fos: FileOutputStream = new FileOutputStream(file)
          var len = zip.read(buffer)
          while (len > 0) {
            fos.write(buffer, 0, len)
            len = zip.read(buffer)
          }
          fos.close
        }
        entry = zip.getNextEntry
      }
      true
    } catch {
      case e: Throwable =>
        println(e.getMessage)
        false
    }
  }

  val downloadSettings = Seq(downloadTask, unzipTask)
}
