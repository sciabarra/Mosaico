def norm(s: String) = s.replace("-", "_")

lazy val genDockerSubs = taskKey[Unit]("genDockerSubs")

genDockerSubs := {

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

addCommandAlias("genDocker", "; genDockerSubs ; reload")
