package mosaico.docker

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import sbt._, Keys._
import sbt.plugins.JvmPlugin
import sbtdocker.DockerPlugin

object MosaicoDockerPlugin
  extends AutoPlugin
    with DockerSettings
    with DownloadSettings
    with AlpineSettings {


  override def requires = JvmPlugin && DockerPlugin

  val autoImport = MosaicoDockerKeys

  import autoImport._

  override val projectSettings =
    dockerSettings ++ alpineSettings ++ downloadSettings

}
