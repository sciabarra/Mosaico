package mosaico.toplevel

import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin

/**
  * Created by msciab on 13/09/16.
  */
object MosaicoToplevelPlugin
  extends AutoPlugin {

  object Keys {
    lazy val genDockerSubs = taskKey[Unit]("genDockerSubs")
  }

  val genDockerSubsTask = Keys.genDockerSubs := {
    def norm(s: String) = s.replace("-", "_")
    val folders = (file(".") ** "docker.sbt").get.map(_.getParentFile)
    val projects = folders.map(_.getName).map(norm)
    val projectDefs = folders.
      map(x =>
        s"""lazy val ${norm(x.getName)} = (project in file("${x}")).
            |enablePlugins(MosaicoPlugin)""".stripMargin.replaceAll("\n", ""))

    IO.write(baseDirectory.value / "subprojects.sbt",
      s"""|${projectDefs.mkString("\n\n")}
          |
        |addCommandAlias("dockerBuildAll", "; ${projects.mkString("/docker ; ")}/docker")
    """.stripMargin)

    println("*** Docker subprojects:")
    projects.foreach(println)

  }

  val autoImport = Keys
  override def requires = JvmPlugin
  override val projectSettings = Seq(genDockerSubsTask)

}
