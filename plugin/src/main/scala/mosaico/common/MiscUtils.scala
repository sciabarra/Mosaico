package mosaico.common

import java.io.File

import sbt.{ForkOptions, Fork}

/**
  * Created by msciab on 18/09/16.
  */
trait MiscUtils {
  /**
    * Receive an array (e.g "a" "@b" "c")
    * Repace each value starting with "@" with the corresponging key in the map.
    * (e.g. if Map("b" -> "d") returns "a" "d" "c")
    *
    * @param args
    * @param map
    */
  def replaceAtWithMap(args: Seq[String], map: Map[String, String]) = {
    args.map(x =>
      if (x.startsWith("@"))
        map(x.substring(1))
      else x
    )
  }

  // Utils
  def exec(args: Seq[String], home: File, cp: Seq[File]) = {
    Fork.java(ForkOptions(
      runJVMOptions = "-cp" :: cp.map(_.getAbsolutePath).mkString(java.io.File.pathSeparator) :: Nil,
      workingDirectory = Some(home)), args)
  }

  // get a wrapped property
  /*
  def prp(property: String) = {
    val r = System.getProperty(property)
    if (r == null)
      None
    else
      Some(r)
  }*/

  val debugging = System.getProperty("mosaico.debug") != null

  val tracing = Option(System.getProperty("mosaico.trace")).map(x => x.split(",").toSet)

  def debug(msg: String) = {
    if (debugging)
      println(s"%% ${msg}")
  }

  def trace(what: String, msg: String) = {
    tracing.map { set =>
      if (set(what))
        println(s"%%% ${msg}")
    }
  }
}
