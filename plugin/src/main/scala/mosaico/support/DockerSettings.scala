package mosaico.support


import sbt._, Keys._

import SupportPlugin.autoImport

/**
  * placeholder for docker related tasks
  * currently empty
  */

trait DockerSettings {
  this: AutoPlugin =>

  trait DockerKeys {
    lazy val dk = inputKey[Unit]("dk")
    lazy val dki = inputKey[Unit]("dki")
    lazy val dkrm = inputKey[Unit]("dkrm")
  }

  import autoImport._

  /**
    * Find images by name
    *
    * Returns a list of "name" -> "id"
    *
    * @param name
    * @return
    */
  def findImagesByName(name: String) = {
    val s1 = "docker images".!!
      .split("\n").toList.tail
    //s1=List("mosaico/abuild   3   3af9 ...."...)
    val s2 = s1.map(_.split("\\s+").toList)
    //s2=List(List("mosaico/abuild","3","3af9",...)...)
    val s3 = s2.map(x => x(0) + ":" + x(1) -> x(2))
    //List( "mosaico/abuild:3" -> "3af9",....)
    s3.filter(_._1.indexOf(name) != -1)
    // filter with first
  }

  //map an empty array in an array with one empty arg
  def zero2one(args: Seq[String]) =
    if (args.length == 0) Seq("")
    else args


  /**
    * Run docker from command line
    */

  val _dk = dk :=
    ("docker" +: Def.spaceDelimited("<arg>").parsed)
      .mkString(" ").!

  /*
   * list images with substring specified in the arguments
   */
  val _dki = dki :=
    zero2one(Def.spaceDelimited("<arg>").parsed)
      .flatMap(findImagesByName(_))
      .distinct
      .map(_._1)
      .foreach(println)


  /**
    * Remove images specified in the argument, forced and with no confirmation
    */
  val _dkrm = dkrm :=
    zero2one(Def.spaceDelimited("<arg>").parsed)
      .flatMap(findImagesByName(_))
      .distinct
      .map(_._2)
      .mkString("docker rmi -f ", " ", "")
      .!


  val dockerSettings = Seq(_dk, _dki, _dkrm)

}
