package mosaico.docker

import java.io.File

import sbt._

object MosaicoDockerKeys {

  lazy val alpBuild = inputKey[Seq[File]]("alpBuild")

  lazy val download = inputKey[Option[File]]("download")

  lazy val dki = taskKey[Unit]("dki")

  lazy val amm = inputKey[Unit]("amm")
  lazy val ammPredef = settingKey[String]("ammPredef")
  lazy val ammScripts = settingKey[File]("ammScripts")
  lazy val ammClasspath = taskKey[Seq[File]]("ammClasspath")

}
