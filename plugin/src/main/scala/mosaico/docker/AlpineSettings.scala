package mosaico.docker

import java.io.File

import sbt._, Keys._
import scala.language.postfixOps
import MosaicoDockerPlugin.autoImport

trait AlpineSettings {
  this: AutoPlugin =>

  trait AlpineKeys {
    lazy val alpineBuild = inputKey[Seq[File]]("alpineBuild")
    lazy val alpineBuildImage = settingKey[Option[String]]("alpineBuildImage")
  }

  import autoImport._

  val alpineBuildTask = alpineBuild := {
    val args: Seq[String] = Def.spaceDelimited("<arg>").parsed
    val buildImage = alpineBuildImage.value
    if (buildImage.isEmpty)
      throw new Exception("Please set the alpineBuildImage key to Some(value)")

    if (args.length < 2) {
      println("usage: alpineBuild {APKBUILD} {APKFILE}")
      Seq()
    } else {

      val base = baseDirectory.value
      val target = base / "target"
      val abuild = base / "abuild"
      val in = args(0)
      val inFile = abuild / in
      val out = args(1)
      val outFile = target / out

      val cmd =
        s"""docker run
            | -v ${abuild}:/home/abuild
            | -v ${target}:/home/packager/packages
            | ${buildImage.get} ${in} ${out}
            |""".stripMargin.replace('\n', ' ')

      println(cmd)
      if (inFile.exists) {
        if (!outFile.exists) {
          println(s"*** not found ${outFile.getAbsolutePath} - building")
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
    alpineBuildTask,
    alpineBuildImage := None
  )

}
