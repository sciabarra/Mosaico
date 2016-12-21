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

  def includeExcludeRegex(input: String,
      regex: String*): Boolean = {
    if (regex.isEmpty)
      true
    else regex.toSeq
      .map(x => (x(0) == '+') -> x.tail)
      .map {
        case (result, re) =>
          input.matches(re) -> result
      }
      .map(x => x._1 == x._2)
      .reduce(_ && _)
  }


  /**
    * Ececute a java class with its args,
    * adding a classpath
    * and setting the home directory
    *
    * @param args
    * @param home
    * @param cp
    * @return
    */
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

  val debugging = System.getProperty("debug") != null

  val tracing = Option(System.getProperty("trace")).map(x => x.split(",").toSet)

  /**
    * Print a debug message if "deugging"
    *
    * Debugging is enabled by the "debug" system property
    *
    * @param msg
    */
  def debug(msg: String) = {
    if (debugging)
      println(s"%% ${msg}")
  }

  /**
    * Print a trace message if the specified tracing is enabled
    *
    * Traces are enabled by the "trace=a,b,c" system property
    *
    * The string "a,b,c" will enable traces for "a" "b" and "c"
    *
    * @param what
    * @param msg
    * @return
    */
  def trace(what: String, msg: String) = {
    tracing.map { set =>
      if (set(what))
        println(s"%%% ${msg}")
    }
  }
}
