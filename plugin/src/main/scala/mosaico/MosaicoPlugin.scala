package mosaico.plugin

import sbt.AutoPlugin
import sbt.plugins.JvmPlugin

object MosaicoPlugin
  extends AutoPlugin {

  override def requires = JvmPlugin

  val autoImport = MosaicoKeys
  import MosaicoKeys._

  override val projectSettings = Seq(
   mosaico := { println("hello from mosaico")}
  )
}