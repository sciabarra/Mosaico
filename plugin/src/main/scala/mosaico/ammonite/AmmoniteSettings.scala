package mosaico.ammonite

import java.io.File

import mosaico.docker.MosaicoDockerKeys
import sbt.Keys._
import sbt._

/**
  * Created by msciab on 14/09/16.
  */
trait AmmoniteSettings {
  this: AutoPlugin =>

  import MosaicoDockerKeys._

  val ammTask = amm := {

    val args: Seq[String] = Def.spaceDelimited("<arg>").parsed

    val jvmOpts = Seq(
      "-cp", ammClasspath.value.
        filter(_.exists).map(_.getAbsoluteFile).
        mkString(File.pathSeparator)
    )

    val forkOpt = ForkOptions(
      runJVMOptions = jvmOpts,
      workingDirectory = Some(baseDirectory.value))

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

      script.getAbsolutePath +: args.tail
    }

    //println(jvmOpts ++ runOpts ++ args1)

    Fork.java(forkOpt, runOpts ++ args1)
  }

  val predef =
    """import $ivy.`com.lihaoyi::ammonite-shell:0.7.6`;
       |val shellSession = ammonite.shell.ShellSession ();
       |import shellSession._, ammonite.ops._, ammonite.shell.
    """.stripMargin.replaceAll("\n", " ")

  val ammoniteSettings = Seq(ammTask
    , ivyConfigurations += config("ammonite")
    , libraryDependencies += "com.lihaoyi" % s"ammonite_${
      scalaVersion.value
    }" % "0.7.6" % "ammonite"
    , ammPredef := predef
    , ammScripts := baseDirectory.value
    , ammClasspath <<= update.
      map(_.select(configurationFilter("ammonite")))
  )
}

/* val amm = ammonite.Main(
   """
     |import $ivy.`com.lihaoyi::ammonite-shell:0.7.6`
     |val shellSession = ammonite.shell.ShellSession()
     |import shellSession._,ammonite.ops._,ammonite.shell._
   """.
     stripMargin
   , false
   , new Storage.Folder(Path(baseDirectory.value/"target").toPath)
   , Path(ammScripts.value.toPath)
   )

 amm.runScript(path, args.tail, Seq.empty, true)*/
