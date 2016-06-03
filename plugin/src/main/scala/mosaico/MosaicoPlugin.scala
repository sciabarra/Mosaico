package mosaico.plugin

import sbt._, Keys._
import sbt.plugins.JvmPlugin
import sbtdocker.DockerPlugin

object MosaicoPlugin
  extends AutoPlugin {

  override def requires = JvmPlugin && DockerPlugin

  val autoImport = MosaicoKeys
  import MosaicoKeys._

  override val projectSettings = Seq(
   dki := {
    //  val ls: Seq[String] = "docker images" !!
    //  println(ls)
    println("bah!")
  }
  )
}
