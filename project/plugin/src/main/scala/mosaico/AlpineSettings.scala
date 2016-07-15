package mosaico

import sbt._, Keys._

trait AlpineSettings {
  this: AutoPlugin =>

  import MosaicoKeys._

  val abuildTask = abuild := {
    val args: Seq[String] = Def.spaceDelimited("<arg>").parsed
    if (args.length < 2) {
      println("usage: abuild <APKBUILD> <APKFILE>")
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
              | mosaico/abuild:latest ${in} ${out}
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

  val alpineSettings = Seq(abuildTask)

}