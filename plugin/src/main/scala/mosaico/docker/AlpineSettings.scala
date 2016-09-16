package mosaico.docker

import sbt._, Keys._
import scala.language.postfixOps

trait AlpineSettings {
  this: AutoPlugin =>

  import MosaicoDockerKeys._

  val alpBuildTask = alpBuild := {
    val args: Seq[String] = Def.spaceDelimited("<arg>").parsed
    if (args.length < 3) {
      println("usage: alpBuild {ABUILDIMAGE} {APKBUILD} {APKFILE}")
      Seq()
    } else {
      val image = args(0)
      val base = baseDirectory.value
      val in = args(1)
      val inFile = base / in
      val out = args(2)
      val outFile = base / "target" / out

      if (inFile.exists) {
        val cmd =
          s"""docker run
              | -v ${inFile.getAbsolutePath}:/home/packager/${in}
              | -v ${base}/target:/home/packager/packages
              | ${image} ${in} ${out}
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

  val alpineSettings = Seq(alpBuildTask)

}
