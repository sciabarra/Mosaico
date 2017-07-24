import $exec.Params
import $exec.Ssh

def dkExec(arg: String)(implicit hosts: Tuple2[String,String]) {
  exec(s"docker ${arg}")(hosts._1, hosts._2)
}

def dkCompose(arg: String)(implicit hosts: Tuple2[String,String]){
  scp(pwd/'compose/arg, arg)(hosts._1, hosts._2)
  val name = arg.split('.').head
  dkExec(s"stack deploy -c ${arg} --with-registry-auth ${name}")(hosts._1, hosts._2)
}

def dkSwarm(stackName: String) {
  val master = "master"
  val all = instancesInStack("spark").flatMap(_.name)
  val nodes = all.filter(_ != "master").toSeq
  exec("sudo docker swarm leave --force")(stackName, all.mkString(","))
  val res = execMap("sudo docker swarm init")(stackName, master)
  val out = res(master).get.out.lines
  val cmd = "sudo " + out(4) + "\n" ++ out(5) + "\n" + out(6)
  exec(cmd)(stackName, nodes.mkString(","))
  exec("docker node ls")(stackName, master)
}
