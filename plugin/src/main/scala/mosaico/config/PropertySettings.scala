package mosaico.config

import java.util.Properties
import mosaico.common.FileUtils
import sbt.Keys._
import sbt._

/**
  * Created by msciab on 08/02/15.
  */
trait PropertySettings extends FileUtils {
  this: AutoPlugin =>

  trait PropertyKeys {
    lazy val prpPrefixes = settingKey[Seq[String]]("Property Prefixes")
    lazy val prp = settingKey[Map[String, String]]("Property Map")
  }

  import MosaicoConfigPlugin.autoImport._
  import scala.collection.JavaConverters._

  val profile = Option(System.getProperty("profile")).map(Seq(_)).getOrElse(Nil)

  val prpExtensions = Seq(
    "dist.properties",
    "properties",
    "local.properties") ++
    profile.map(x =>
      s"mosaico.${x}.properties")

  lazy val prpTask = prp := {
    try {
      val prp: Properties = new Properties
      //println("=== Property files ===")
      val loaded = for {
        prpPrefix <- prpPrefixes.value
        prpExt <- prpExtensions
        prpName = s"${prpPrefix}.${prpExt}"
        prpFile = baseDirectory.value / prpName
      } yield {
        if (prpFile.exists)
          prp.load(new java.io.FileInputStream(prpFile))
        //println(prpFile)
        prpFile
      }
      //println("--- Properties --- ")
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


  val prpDumpCmd = Command.args("prpDump", "[-v]") {
    (state, args) =>
      val verbose = args.size==1 && args(0) == "-v"
      val extracted: Extracted = Project.extract(state)
      val prpMap = (prp in extracted.currentRef get extracted.structure.data).get
      val prpPrefs = (prpPrefixes in extracted.currentRef get extracted.structure.data).get
      val base = (baseDirectory in extracted.currentRef get extracted.structure.data).get

      println("=== Property files ===")
      val loaded = for {
        prpPrefix <- prpPrefs
        prpExt <- prpExtensions
        prpName = s"${prpPrefix}.${prpExt}"
        prpFile = base / prpName
      } {
        if (verbose)
          println(s"*** looking for ${prpFile.getAbsolutePath}")
        if (prpFile.exists)
          println(s"${prpName}")
      }
      println("--- Properties --- ")
      for ((k, v) <- prpMap)
        println(s"${k}=${v}")

      state
  }

  val propertySettings = Seq(
    prpPrefixes := Seq("mosaico")
    , prpTask
    , shellPromptTask
    , commands ++= Seq(profileCmd, prpDumpCmd)
  )
}
