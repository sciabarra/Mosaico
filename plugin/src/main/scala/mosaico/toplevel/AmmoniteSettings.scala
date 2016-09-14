package mosaico.toplevel

import ammonite.main.Defaults
import ammonite.ops.Path
import ammonite.runtime.Storage
import sbt.{Def, CrossVersion, AutoPlugin}

/**
  * Created by msciab on 14/09/16.
  */
trait AmmoniteSettings {
  this: AutoPlugin =>

  import MosaicoToplevelPlugin.Keys._

  val ammTask = amm := {
    val args: Seq[String] = Def.spaceDelimited("<arg>").parsed
    if (args.length == 0) {
      println("usage: amm <script> <args>...")
    } else {
      val script = if (args(0).endsWith(".sc")) args(0) else s"${args(0)}.sc"
      val path = Path(ammScripts.value.toPath) / script

      val amm = ammonite.Main(
        """
          |import $ivy.`com.lihaoyi::ammonite-shell:0.7.6`
          |val shellSession = ammonite.shell.ShellSession()
          |import shellSession._,ammonite.ops._,ammonite.shell._
        """.
          stripMargin
        , false
        , new Storage.Folder(Path(baseDirectory.value/"target").toPath)
        , Path(ammScripts.value.toPath)
        )

      amm.runScript(path, args.tail, Seq.empty, true)
    }
  }

  val ammoniteSettings = Seq(ammTask)
}
