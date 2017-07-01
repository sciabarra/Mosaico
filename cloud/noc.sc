
case class Options(group:String = "",
                op:String = "",
                hosts: Seq[String]= Seq.empty)

val parser = new scopt.OptionParser[Options]("noc") {

  head("noc (no cloud)", "0.1")

  opt[String]('c', "cluster").action {
    (x, c) => c.copy(group=x)
  }.text("cluster name")

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
       case "create" => create(opts)
       case _ => println("nothign to do")
     }
  }
}

def create(opts: Options) = {
  println("creatig "+opts.hosts)
}
