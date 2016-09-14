package mosaico

import mosaico.docker.MosaicoDockerPlugin
import mosaico.toplevel.MosaicoToplevelPlugin
import sbt.AutoPlugin
import sbt.plugins.JvmPlugin
import sbtdocker.DockerPlugin

/**
  * Created by msciab on 13/09/16.
  */
object MosaicoPlugin
  extends AutoPlugin {

  override def requires =
    JvmPlugin &&
      DockerPlugin &&
      MosaicoDockerPlugin
}
