package mosaico.docker

import sbt._, Keys._
import scala.language.postfixOps

/**
  * placeholder for docker related tasks
  * currently empty
  */

trait DockerSettings {
  this: AutoPlugin =>

  trait DockerKeys {

  }

  import MosaicoDockerPlugin.autoImport._

  val dockerSettings = Seq()

}
