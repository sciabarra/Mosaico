case class Options(
                dry: Boolean = false,
                stack:String = "cluster",
                op:String = "help",
                hosts: Seq[String] = Seq.empty)

implicit val defaults = Options()

val parser = new scopt.OptionParser[Options]("clu") {

  head("clu (cluster)", "0.1")

  opt[Unit]("dry-run").action {
     (_, c) => c.copy(dry=true)
  }.text("dry run")

  opt[String]('s', "stack").action(
    (x, c) => c.copy(stack=x)
  ).text(s"stack name, default: '${defaults.stack}'")

  cmd("create").text("create is a command")
  .action( (_,c) => c.copy(op="create"))
  .children {
    arg[String]("<hosts>...").unbounded().action {
      (x,c) => c.copy(hosts = c.hosts :+ x)
    }
  }
  help("help").text("this text")
}

@main def main(args: String*) : Unit =  {
  parser.parse(args, Options()).map {
    opts =>
     opts.op match {
       case "create" => create(opts.hosts)(opts)
       case _ => println("use --help for help")
     }
  }
}

def create(hosts: Seq[String])(implicit opts: Options) = {
  println(s"stack: ${opts.stack}")
  println(s"""creating: ${hosts.mkString(",")}""")
}
