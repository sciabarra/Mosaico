import sbt._,Keys._

object Subprojects extends Build with DockerSubprojects {

   lazy val root = project.in(file("."))

}
