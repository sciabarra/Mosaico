package mosaico.docker

import sbt._, Keys._
import scala.language.postfixOps

// placeholder for docker related tasks
trait DockerSettings {
  this: AutoPlugin =>

  import MosaicoDockerKeys._

  val dockerSettings = Seq()

}
