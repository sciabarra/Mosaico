package mosaico.toplevel

import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin

/**
  * Created by msciab on 13/09/16.
  */
object MosaicoToplevelPlugin
  extends AutoPlugin
    with GenerateSettings
    with AmmoniteSettings {

  object Keys {
    lazy val amm = inputKey[Unit]("amm")
    lazy val ammScripts = settingKey[File]("ammScripts")
    lazy val genDockerSubs = taskKey[Unit]("genDockerSubs")
  }

  override def requires = JvmPlugin
  val autoImport = Keys
  import Keys._

  override val projectSettings = Seq(
    ammScripts := baseDirectory.value
  ) ++ ammoniteSettings ++ generateSettings

}
