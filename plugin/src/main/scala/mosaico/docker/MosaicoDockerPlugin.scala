package mosaico.docker

import sbt._, Keys._
import sbt.plugins.JvmPlugin
import sbtdocker.DockerPlugin
import mosaico.config.MosaicoConfigPlugin

/**
  * Plugin for docker builds, see documentation for details.
  */
object MosaicoDockerPlugin
  extends AutoPlugin
    with DockerSettings
    with DownloadSettings
    with AlpineSettings {

  object autoImport
    extends AlpineKeys
      with DockerKeys
      with DownloadKeys

  override val projectSettings =
    dockerSettings ++
      downloadSettings ++
      alpineSettings

  override def requires =
    JvmPlugin &&
      DockerPlugin &&
      MosaicoConfigPlugin
}
