package mosaico.docker

import sbt._, Keys._

trait DockerSettings {
  this: AutoPlugin =>

  import MosaicoDockerKeys._

  val dkiTask = dki := {
    "docker images --format '{{.ID}} {{.Repository}}:{{.Tag}}'" !
  }

  val dockerSettings = Seq(dkiTask)

}
