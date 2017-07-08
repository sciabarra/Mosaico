import ammonite.ops._

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
  if(!opts.dry) {
      val inventory = Seq(
        "[all:vars]",
        s"master_public_ip=${hosts.head}",
        s"master_private_ip=${hosts.head}") ++
        Seq("[masters]", s"${hosts.head} local_ip=${hosts.head}") ++
        Seq("[nodes]") ++
        hosts.tail.map(t => s"${t} local_ip=${t}")

    val inv = pwd / 'conf / s"${opts.stack}.inv"
    rm ! inv
    write(inv, inventory.mkString("\n"))
    println("wrote " + inv)
    inv.toString
  }
}

val preInitialize = """
ssh-copy-id id_rsa root@spark.sciabarra.net ;
ssh root@spark.sciabarra.net "useradd centos ;
mkdir /home/centos/.ssh ;
cp /root/.ssh/authorized_keys /home/centos/.ssh ;
chmod 0700 /home/centos/.ssh ;
chmod 0600 /home/centos/.ssh/authorized_keys ;
chown -Rvf centos /home/centos/.ssh/ ;
echo -e 'centos\tALL=(ALL)\tNOPASSWD: ALL' >>/etc/sudoers
"""
