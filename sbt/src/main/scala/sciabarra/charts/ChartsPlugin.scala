package sciabarra.charts

import sbt._, Keys._
import sbt.plugins.JvmPlugin
import sbtdocker.DockerPlugin

object ChartsPlugin
  extends AutoPlugin
  with DockerSettings
  with AlpineSettings {

  override def requires = JvmPlugin && DockerPlugin

  val autoImport = ChartsKeys

  import ChartsKeys._

  override val projectSettings =
    dockerSettings ++ alpineSettings

}
