package mosaico.config

import java.util.Properties
import mosaico.common.{MiscUtils, FileUtils}
import sbt.Keys._
import sbt._

/**
  * Settings for properties see documentation for details
  *
  */
trait PropertySettings extends FileUtils with MiscUtils {
  this: AutoPlugin =>

  trait PropertyKeys {
    lazy val prp = settingKey[Map[String, String]]("Property Map")
    lazy val prpLookup = settingKey[Seq[(File, String)]]("Where to lookup for property files")
  }

  import MosaicoConfigPlugin.autoImport._
  import scala.collection.JavaConverters._

  val profile = Option(System.getProperty("profile")).map(Seq(_)).getOrElse(Nil)

  val prpExtensions = Seq(
    "dist.properties",
    "properties",
    "local.properties") ++
    profile.map(x =>
      s"${x}.properties")

  lazy val prpTask = prp := {
    try {
      val prp: Properties = new Properties
      val loaded = for {
        (prpDir, prpPrefix) <- prpLookup.value
        prpExt <- prpExtensions
        prpName = s"${prpPrefix}.${prpExt}"
        prpFile = prpDir / prpName
      } yield {
        if (prpFile.exists) {
          //print(s"loading ${prpFile.getName}")
          prp.load(new java.io.FileInputStream(prpFile))
          val filename = prpFile.getAbsolutePath
          trace("prp", s"loaded ${filename}")
        }
        prpFile
      }
      prp.asScala.toMap
    } catch {
      case _: Throwable => Map()
    }
  }


  def profileCmd = Command.args("profile", "<args>") { (state, args) =>
    if (args.size != 1) {
      println("usage: profile <profile>|- (- = noprofile)")
      state
    } else {
      val (profile, profileSetter) =
        if (args.head == "-")
          "" -> """System.getProperties.remove("profile")"""
        else
          args.head + "." -> s"""System.setProperty("profile", "${args.head}")"""
      val prp = state.configuration.baseDirectory / s"agilesites${profile}.properties"
      state.copy(remainingCommands =
        Seq( s"""eval ${profileSetter} """, "reload") ++ state.remainingCommands)
    }
  }

  // display a prompt with the project name and the profile
  lazy val shellPromptTask = shellPrompt in ThisBuild := {
    state =>
      Project.extract(state).currentRef.project +
        Option(System.getProperty("profile")).map("[" + _ + "]> ").getOrElse("> ")
  }

  val propertySettings = Seq(
    prpLookup := Seq(baseDirectory.value -> "mosaico")
    , prpTask
    , shellPromptTask
    , commands ++= Seq(profileCmd)
  )
}
