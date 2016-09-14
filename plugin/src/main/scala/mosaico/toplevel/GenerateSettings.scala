package mosaico.toplevel

import sbt.Keys._
import sbt._

/**
  * Created by msciab on 14/09/16.
  */
trait GenerateSettings {
  this : AutoPlugin =>
  import MosaicoToplevelPlugin.Keys._

  /**
    * Generate Subtasks
    */
  val genDockerSubsTask = genDockerSubs := {
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
    println("*** Generated subprojects.sbt, please reload")

  }


  val generateSettings = Seq(genDockerSubsTask)
}
