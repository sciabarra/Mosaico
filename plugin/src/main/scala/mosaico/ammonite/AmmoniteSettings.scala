package mosaico.ammonite

import java.io.{FileNotFoundException, File}

import sbt.Keys._
import sbt._

/**
  * Created by msciab on 14/09/16.
  */
trait AmmoniteSettings {
  this: AutoPlugin =>

  import MosaicoAmmoniteKeys._

  val ammTask = amm := {

    val args: Seq[String] = Def.spaceDelimited("<arg>").parsed

    val jvmOpts = Seq(
      "-cp", ammClasspath.value.
        filter(_.exists).map(_.getAbsoluteFile).
        mkString(File.pathSeparator)
    )

    val forkOpt = ForkOptions(
      runJVMOptions = jvmOpts,
      workingDirectory = Some(baseDirectory.value),
      connectInput = true)

    val runOpts = Seq("ammonite.Main"
      , "--no-default-predef"
      , "-p", ammPredef.value
      , "-h", (baseDirectory.value / "target" / "amm").getAbsolutePath
    )

    val args1 = if (args.length == 0) args
    else {
      val script: File =
        if (args(0).endsWith(".sc"))
          ammScripts.value / args(0)
        else
          ammScripts.value / s"${args(0)}.sc"

      if(!script.exists())
        throw new FileNotFoundException(s"not found ${script}")

      script.getAbsolutePath +: args.tail
    }

    //println(jvmOpts ++ runOpts ++ args1)

    Fork.java(forkOpt, runOpts ++ args1)
  }

  val predef =
    """import $ivy.`com.lihaoyi::ammonite-shell:0.8.1`;
      |val shellSession = ammonite.shell.ShellSession ();
      |import shellSession._, ammonite.ops._, ammonite.shell._
    """.stripMargin.replaceAll("[\\n\\r]", "")

  val ammoniteSettings = Seq(ammTask
    , ivyConfigurations += config("ammonite")
    , libraryDependencies += "com.lihaoyi" % s"ammonite_2.10.5" % "0.8.1" % "ammonite"
    , ammPredef := predef
    , ammScripts := baseDirectory.value
    , ammClasspath <<= update.
      map(_.select(configurationFilter("ammonite")))
  )
}
