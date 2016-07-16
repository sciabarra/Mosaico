package sciabarra.charts

import java.io.File

import sbt._

object ChartsKeys {

  lazy val abuild = inputKey[Seq[File]]("abuild")

  lazy val dki = taskKey[Unit]("dki")

}
