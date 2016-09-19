package mosaico.config

import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

/**
  * Plugin for configurations, see documentation for details.
  */
object MosaicoConfigPlugin
  extends AutoPlugin
    with PropertySettings
    with VersionSettings {

  override def requires = JvmPlugin

  object autoImport extends
    PropertyKeys
    with VersionKeys

  def shCommand = Command.args("sh", "<shell command>") { (state, args) =>
    import scala.sys.process._
    args.mkString(" ").!
    state
  }

  override val projectSettings = Seq(commands += shCommand) ++
    propertySettings ++
    versionSettings
}