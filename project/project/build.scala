import sbt._, Keys._

object build extends Build {

  val generateProjectsTask = Def.task {
    // generate list of project declaration
    val projects = (file(".") ** "docker.sbt" ).get.
      map(_.getParentFile).map(x =>
      s"""lazy val ${x.getName.replace("-","_")} =
         |(project in file("${x.getAbsolutePath}")).
         |enablePlugins(DockerPlugin,MosaicoDockerPlugin)""".stripMargin)

    // create the output file
    val output = (sourceManaged in Compile).value / "DockerSubprojects.scala"
    //println(output.getAbsolutePath)
    IO.write(output, s"""
import sbt._,Keys._
import sbtdocker.DockerPlugin,mosaico.docker.MosaicoDockerPlugin
trait DockerSubprojects { this: Build =>
${projects.mkString("\n")}
}""")
    Seq(output)
  }

  lazy val root = project.in(file(".")).settings(
    sourceGenerators in Compile <+= generateProjectsTask
  )
}
