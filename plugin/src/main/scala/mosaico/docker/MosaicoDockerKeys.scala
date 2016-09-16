package mosaico.docker

import java.io.File

import sbt._

object MosaicoDockerKeys {

  lazy val alpBuild = inputKey[Seq[File]]("alpBuild")

  lazy val alpBuildImage = settingKey[Option[String]]("alpBuildImage")

  lazy val download = inputKey[Option[File]]("download")

}
