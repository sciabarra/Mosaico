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
    with DownloadSettings
    with AlpineSettings
    with UnpackSettings {

  object autoImport
    extends AlpineKeys
      with DownloadKeys
      with UnpackKeys

  override val projectSettings =
    downloadSettings ++
      unpackSettings ++
      alpineSettings

  override def requires =
    JvmPlugin &&
      DockerPlugin &&
      MosaicoConfigPlugin
}
