package mosaico.ammonite

import java.io.File

import sbt._

object MosaicoAmmoniteKeys {

  lazy val amm = inputKey[Unit]("amm")
  lazy val ammPredef = settingKey[String]("ammPredef")
  lazy val ammScripts = settingKey[File]("ammScripts")
  lazy val ammClasspath = taskKey[Seq[File]]("ammClasspath")

}
