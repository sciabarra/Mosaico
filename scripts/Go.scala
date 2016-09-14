import ammonite.ops._

object Go extends App {

  val go = pwd/'scripts/"go.sc"
  val amm = ammonite.Main(
    """
      |import $ivy.`com.lihaoyi::ammonite-shell:0.7.6`
      |val shellSession = ammonite.shell.ShellSession()
      |import shellSession._,ammonite.ops._,ammonite.shell._
    """.stripMargin, false)

  amm.runScript(go, Seq.empty, Seq.empty, true)
}