package mosaico.docker

import java.io.File

import sbt._

object MosaicoDockerKeys {

  lazy val alpBuild = inputKey[Seq[File]]("alpBuild")

  lazy val download = inputKey[Option[File]]("download")

  lazy val dki = taskKey[Unit]("dki")
}
