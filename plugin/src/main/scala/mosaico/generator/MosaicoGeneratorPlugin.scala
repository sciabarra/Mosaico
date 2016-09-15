package mosaico.generator

import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin

/**
  * Created by msciab on 13/09/16.
  */
object MosaicoGeneratorPlugin
  extends AutoPlugin {

  override def requires = JvmPlugin

  object Keys {
    lazy val dockers = taskKey[Unit]("dockers")
  }
  val autoImport = Keys
  import Keys._

  /**
    * Generate Subtasks
    */
  val dockersTask = dockers := {
    def norm(s: String) = s.replace("-", "_")
    val folders = (file(".") ** "docker.sbt").get.map(_.getParentFile)
    val projects = folders.map(_.getName).map(norm)
    val projectDefs = folders.
      map(x =>
        s"""lazy val ${norm(x.getName)} = (project in file("${x}")).
            |enablePlugins(MosaicoDockerPlugin,MosaicoAmmonitePlugin)""".stripMargin.replaceAll("\n", ""))

    IO.write(baseDirectory.value / "dockers.sbt",
      s"""|${projectDefs.mkString("\n\n")}
          |
          |addCommandAlias("dockers", "; ${projects.mkString("/docker ; ")}/docker")
    """.stripMargin)

    println("*** Docker subprojects found:")
    projects.foreach(println)
    println("*** Generated dockers.sbt, please reload")

  }

  override val projectSettings = Seq(dockersTask)
}
