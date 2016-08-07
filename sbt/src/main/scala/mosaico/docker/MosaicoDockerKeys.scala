package mosaico.docker

import java.io.File

import sbt._

object MosaicoDockerKeys {

  lazy val abuild = inputKey[Seq[File]]("abuild")

  lazy val download = inputKey[Option[File]]("download")

  lazy val dki = taskKey[Unit]("dki")

}
