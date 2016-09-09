import sbt._,Keys._

object build extends Build with DockerSubprojects {

  lazy val root = project.in(file(".")).dependsOn(file("sbt").toURI)

}


