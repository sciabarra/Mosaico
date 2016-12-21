package mosaico.docker

import sbt._, Keys._
import org.rauschig.jarchivelib._
import java.io._
import java.util.zip.ZipInputStream
import mosaico.common.MiscUtils

trait UnpackSettings
  extends MiscUtils {
  this: AutoPlugin =>

  trait UnpackKeys {
    lazy val unzip = inputKey[Boolean]("unzip")
    lazy val untar = inputKey[Boolean]("untar")
  }

  import mosaico.config.MosaicoConfigPlugin.autoImport._
  import mosaico.docker.MosaicoDockerPlugin.autoImport._

  val _unzip = unzip := {
    try {
      val args: Seq[String] = replaceAtWithMap(Def.spaceDelimited("<arg>").parsed, prp.value)
      if (args.length < 2)
        throw new Exception("unzip files from base directory in the target folder\nusage: unzip <file.zip> <target>")
      val zipFile = baseDirectory.value / args(0)
      var outDir = baseDirectory.value / args(1)
      val filter = args.tail.tail
      var zip = new ZipInputStream(new FileInputStream(zipFile))
      val buffer: Array[Byte] = new Array[Byte](1024)
      var entry = zip.getNextEntry
      while (entry != null) {
        val input = entry.getName
        if (!includeExcludeRegex(input, filter: _*))
          println(s"-${input}")
        else {
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
          println(s"+${input}")
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

  val _untar = untar := {
    try {
      val args: Seq[String] = replaceAtWithMap(Def.spaceDelimited("<arg>").parsed, prp.value)
      if (args.length < 2)
        throw new Exception("usage: <file.tgz> <outdir> [exclude-regex]")

      val infile = baseDirectory.value / args(0)
      val outdir = baseDirectory.value / args(1)
      val filter = args.tail.tail

      //"jdk1.8.0_\\d+/(src\\.zip|javafx-src\\.zip.*|db/.*|man/.*|include/.*|lib/(missioncontrol|visualvm)/.*|jre/lib/desktop/.*)".r

      //println(infile)
      //println(outdir)
      //println(exclude)
      if (!infile.exists || outdir.exists)
        throw new Exception("input missing or outdir exists")

      val arc = ArchiverFactory.createArchiver(infile)
      val str = arc.stream(infile)
      var ent = str.getNextEntry
      outdir.mkdirs
      while (ent != null) {
        val curr = ent.getName
        if (includeExcludeRegex(curr, filter: _*)) {
          println(s"+${curr}")
          ent.extract(outdir)
        } else {
          println(s"-${curr}")
        }
        ent = str.getNextEntry
      }
      true
    } catch {
      case e: Exception =>
        println(e.getMessage)
        false
    }
  }

  val unpackSettings = Seq(_unzip, _untar)
}