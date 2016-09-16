package mosaico.docker

import sbt._, Keys._
import scala.language.postfixOps

trait AlpineSettings {
  this: AutoPlugin =>

  import MosaicoDockerKeys._

  val alpBuildTask = alpBuild := {
    val args: Seq[String] = Def.spaceDelimited("<arg>").parsed
    val buildImage = alpBuildImage.value
    if(buildImage.isEmpty)
      throw new Exception("Please set the alpBuildImage key to Some(value)")
    if (args.length < 2) {
      println("usage: alpBuild {APKBUILD} {APKFILE}")
      Seq()
    } else {
      val base = baseDirectory.value
      val in = args(0)
      val inFile = base / in
      val out = args(1)
      val outFile = base / "target" / out

      if (inFile.exists) {
        val cmd =
          s"""docker run
              | -v ${inFile.getAbsolutePath}:/home/packager/${in}
              | -v ${base}/target:/home/packager/packages
              | ${buildImage.get} ${in} ${out}
              |""".stripMargin.replace('\n', ' ')

        if (!outFile.exists) {
          println(cmd)
          cmd !
        }

        if (outFile.exists)
          Seq(outFile)
        else {
          println("cannot build outfile")
          Seq()
        }
      } else {
        println(s"not found ${in}")
        Seq()
      }
    }
  }

  val alpineSettings = Seq(
    alpBuildTask,
    alpBuildImage := None
  )

}
