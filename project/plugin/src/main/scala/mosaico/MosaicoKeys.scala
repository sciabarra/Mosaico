package mosaico

import java.io.File

import sbt._

object MosaicoKeys {

  lazy val dki = taskKey[Unit]("dki")

  lazy val abuild = inputKey[Seq[File]]("abuild")

}
