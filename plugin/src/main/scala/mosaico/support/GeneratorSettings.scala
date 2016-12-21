package mosaico.support

import sbt._,Keys._
import SupportPlugin.autoImport

/**
  * Plugin for generating a  dependencies sbt.
  * Saves time from writing it manually.
  */
trait GeneratorSettings {
  this: AutoPlugin =>

  trait GeneratorKeys {
    lazy val genDeps = taskKey[Unit]("genDeps")
  }

  import autoImport._

  /**
    * Generate Subtasks
    */
  val genDepsTask = genDeps := {
    def norm(s: String) = s.replace("-", "_")
    val folders = (file(".") ** "docker.sbt").get.map(_.getParentFile)
    val projects = folders.map(_.getName).map(norm)
    val projectDefs = folders.
      map(folder =>
        s"""lazy val ${norm(folder.getName)} = (project in file("${folder}"))
            |.enablePlugins(MosaicoDockerPlugin)
            |""".stripMargin.replaceAll("[\\n\\r]", ""))


    IO.write(baseDirectory.value / "deps.sbt",
      s"""|${projectDefs.mkString("\n\n")}
          |
          |addCommandAlias("dockers", "; ${projects.mkString("/docker ; ")}/docker")
    """.stripMargin)

    println("*** Docker subprojects found:")
    projects.foreach(println)
    println("*** Generated deps.sbt, please reload")
  }

  val generatorSettings = Seq(genDepsTask)
}
