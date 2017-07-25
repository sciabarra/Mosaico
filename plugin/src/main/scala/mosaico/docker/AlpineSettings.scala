package mosaico.docker

import java.io.File
import mosaico.common.MiscUtils
import mosaico.config.MosaicoConfigPlugin
import sbt._, Keys._
import scala.language.postfixOps
import MosaicoDockerPlugin.autoImport

/**
  * Alpine build settings see documentation for details
  */
trait AlpineSettings extends MiscUtils {
  this: AutoPlugin =>

  trait AlpineKeys {
    lazy val alpineBuild = inputKey[Seq[File]]("alpineBuild")
  }

  import autoImport._
  import MosaicoConfigPlugin.autoImport._

  val alpineBuildTask = alpineBuild := {
    val args: Seq[String] = replaceAtWithMap(Def.spaceDelimited("<arg>").parsed, prp.value)

    if (args.length < 3) {
      println("usage: alpineBuild {ALPINEBUILDIMAGE} {APKBUILD} {APKFILE}")
      Seq()
    } else {
      val base = baseDirectory.value
      val buildImage = args(0)
      (base/"abuild").mkdirs
      (base/"target").mkdirs
      val target = (base / "target").getAbsolutePath.replace('\\', '/') // damn windows!
      val abuild = (base / "abuild").getAbsolutePath.replace('\\', '/')
      val in = args(1)
      val inFile = base / "abuild" / in
      val out = args(2)
      val outFile = base / "target" / out
      val cmd =
        s"""docker run
           | -v ${abuild}:/home/abuild
           | -v ${target}:/home/target
           | ${buildImage} ${in} ${out}
           |""".stripMargin.replaceAll("[\\r\\n]", "")

      if (inFile.exists) {
        if (!outFile.exists) {
          println(s"*** not found ${outFile.getAbsolutePath} - building")
          println(cmd)
          cmd !
        } else {
          println(s"*** found ${outFile} - not building")
        }
        Seq(outFile)
      } else {
        println(s"*** not found ${inFile}")
        Seq()
      }
    }
  }

  val alpineSettings = Seq(
    alpineBuildTask
  )

}
