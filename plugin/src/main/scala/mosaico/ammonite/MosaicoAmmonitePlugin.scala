package mosaico.ammonite

import sbt._
import sbt.plugins.JvmPlugin
import sbtdocker.DockerPlugin

object MosaicoAmmonitePlugin
  extends AutoPlugin
    with AmmoniteSettings {

  override def requires = JvmPlugin

  val autoImport = MosaicoAmmoniteKeys

  override val projectSettings =
      ammoniteSettings

}
