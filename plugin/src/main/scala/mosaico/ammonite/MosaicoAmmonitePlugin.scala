package mosaico.ammonite

import java.io.File

import sbt._
import Keys._
import sbt.plugins.JvmPlugin

object MosaicoAmmonitePlugin
  extends AutoPlugin {

  object autoImport {
    val ammVersion = settingKey[(String, String)]("Ammonite Version")
    val ammPredef = settingKey[Option[String]]("Ammonite predef")
    val amm = inputKey[Unit]("Ammonite Launch Command")
  }

  import autoImport._

  val ammTask = amm := {
    val args = Def.spaceDelimited("<arg>").parsed
    val home = baseDirectory.value
    val classpath = (fullClasspath in Compile).value.files

    val predef: Seq[String] = if (ammPredef.value.isEmpty) Seq()
    else Seq("-p", ammPredef.value.getOrElse(""))

    val jvmOpts = Seq("-cp"
      , classpath.map(_.getAbsolutePath).mkString(File.pathSeparator)
      , "ammonite.Main")

    val forkArgs =
      Seq("-h", home.getAbsolutePath) ++
        predef ++ args

    val forkOpts = ForkOptions(
      runJVMOptions = jvmOpts,
      connectInput = true,
      outputStrategy = Some(StdoutOutput),
      envVars = Map.empty,
      workingDirectory = Some(home))

    Fork.java(forkOpts, forkArgs)
  }

  override val projectSettings = Seq(
    ammVersion := "1.0.0-RC9" -> "2.12.2"
    , scalaVersion := ammVersion.value._2
    , ammPredef := None
    , libraryDependencies += "com.lihaoyi" % s"ammonite_${ammVersion.value._2}" % ammVersion.value._1
    , ammTask
  )

  override def requires =
    JvmPlugin
}
