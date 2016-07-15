package mosaico

import sbt._, Keys._
import sbt.plugins.JvmPlugin
import sbtdocker.DockerPlugin

object MosaicoPlugin
  extends AutoPlugin
  with DockerSettings
  with AlpineSettings {

  override def requires = JvmPlugin && DockerPlugin

  val autoImport = MosaicoKeys

  import MosaicoKeys._

  override val projectSettings =
    dockerSettings ++ alpineSettings

}
