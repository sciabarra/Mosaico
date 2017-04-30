import $exec.lib.Ec2
import $exec.lib.CloudFormation
import $exec.lib.Ssh
import scala.io._

@main def inventory(stackName: String) = {
  val filtered = instancesInStack(stackName)
  val master = filtered.filter(_.state == "running").filter(_.name.getOrElse("") == "master")
  val nodes = filtered.filter(_.state == "running").filter(_.name.getOrElse("") != "master")

  val inventory = Seq(
    "[all:vars]",
    s"master_public_ip=${master.head.publicIp}",
    s"master_private_ip=${master.head.privateIp}") ++
    Seq("[masters]", s"${master.head.publicIp} local_ip=${master.head.privateIp}") ++
    Seq("[nodes]") ++
    nodes.map(t => s"${t.publicIp} local_ip=${t.privateIp}")

  val inv = pwd / 'conf / s"${stackName}.inv"
  rm ! inv
  write(inv, inventory.mkString("\n"))
  println("wrote " + inv)
  inv.toString
}

@main def create(stackName: String) = {
  val cfr = cfCreateRequest(stackName)
  val body = Source.fromFile(template.toString).getLines.mkString("\n")
  cfr.setTemplateBody(body)
  // run request
  cf.createStack(cfr)
  cfStatusLoop(stackName)
  inventory(stackName)
}

@main def delete(stackName: String) = {
  cf.deleteStack(cfDeleteRequest(stackName))
  cfStatusLoop(stackName)
}

@main def list(stackName: String) = {
  val instances = instancesInStack(stackName, false)
  println("ID\t\t\tNAME\tSTATE\tPRIVIP\t\tPUBIP")
  for (i <- instances)
    println(s"${i.id}\t${i.name.getOrElse("-")}\t${i.state}\t${i.privateIp}\t${i.publicIp}")
}

@main def ssh(stackName: String, names: String, args: String*) = {
  val ips = ipsByName(stackName, names)
  for (ip <- ips) {
    println(s">>> ${ip} <<<")
    val exe = Seq("ssh",
      "-i", "id_rsa",
      "-o", "UserKnownHostsFile=/dev/null",
      "-o", "StrictHostKeyChecking=no",
      s"centos@${ip}") :+ args.mkString(" ; ")
    val res = scala.util.Try(%(exe)(pwd))
    println(s"<<< ${res} >>>")
  }
}

@main def start(stackName: String) = {
  ec2start(instancesInStack(stackName, false).map(_.id): _*)
}

@main def stop(stackName: String) = {
  ec2stop(instancesInStack(stackName).map(_.id): _*)
}

@main def swarm(stackName: String, master: String, nodes: Seq[String]) {
  exec("sudo docker swarm leave --force")(stackName, master)
  val res = execMap("sudo docker swarm init")(stackName, master)
  val out = res(master).get.out.lines
  val cmd = "sudo docker swarm leave ; sudo " + out(4) + "\n" ++ out(5) + "\n" + out(6)
  exec(cmd)(stackName, nodes.mkString(","))
}

@main def ansible(stackName: String, file:String="site.yml"): Unit =
  %("ansible-playbook", "-i", s"${inventory(stackName)}",  s"ansible/${file}")(pwd)
