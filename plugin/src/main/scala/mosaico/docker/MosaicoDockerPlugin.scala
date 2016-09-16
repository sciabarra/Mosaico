package mosaico.docker

import sbt._, Keys._
import sbt.plugins.JvmPlugin
import sbtdocker.DockerPlugin
import mosaico.ammonite.{MosaicoAmmonitePlugin, AmmoniteSettings}

object MosaicoDockerPlugin
  extends AutoPlugin
    with DockerSettings
    with DownloadSettings
    with AlpineSettings
    with AmmoniteSettings {

  override def requires =
    JvmPlugin &&
      DockerPlugin

  val autoImport = MosaicoDockerKeys

  import autoImport._

  override val projectSettings =
    dockerSettings ++
      downloadSettings ++
      alpineSettings ++
      ammoniteSettings

}
