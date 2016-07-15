package mosaico

import sbt._, Keys._

trait DockerSettings {
  this: AutoPlugin =>

  import MosaicoKeys._

  val dkiTask = dki := {
    "docker images --format '{{.ID}} {{.Repository}}:{{.Tag}}'" !
  }

  val dockerSettings = Seq(dkiTask)


}
