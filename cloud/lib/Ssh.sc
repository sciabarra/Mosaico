import $exec.Params
import $exec.CloudFormation
import $exec.Ec2
import ammonite.ops._

def instancesInStack(stackName: String, running: Boolean = true) = {
  val insts = ec2instances(running)
  val ids = cfInstanceIds(stackName)
  insts.filter(i => ids.exists(i.id == _))
}

def ipsByName(stackName: String, names: String) = {
  val instances = instancesInStack(stackName)
  val name1 = "," + names + ","
  instances.filter(x => name1.indexOf(s",${x.name.getOrElse("")},") != -1).
    map(_.publicIp)
}

def ipsByName2(stackName: String, names: String) = {
  val instances = instancesInStack(stackName)
  val name1 = "," + names + ","
  instances.filter(x => name1.indexOf(s",${x.name.getOrElse("")},") != -1).
    map(i => i.name.getOrElse("") -> i.publicIp)
}

def execMap(args: String*)(implicit hosts: Tuple2[String,String]) = {
  val res = for {
    (name, ip) <- ipsByName2(hosts._1, hosts._2)
    exe = Seq("ssh",
      "-i", "id_rsa",
      "-o", "UserKnownHostsFile=/dev/null",
      "-o", "StrictHostKeyChecking=no",
      s"${imageUser}@${ip}") :+ args.mkString(" ; ")
  } yield {
    name -> scala.util.Try(%%(exe)(pwd))
  }
  res.toMap
}

def exec(args: String*)(implicit hosts: Tuple2[String,String]) {
  for {
    ip <- ipsByName(hosts._1, hosts._2)
    exe = Seq("ssh",
      "-i", "id_rsa",
      "-o", "UserKnownHostsFile=/dev/null",
      "-o", "StrictHostKeyChecking=no",
      s"${imageUser}@${ip}") :+ args.mkString(" ; ")
  } {
    println(exe)
    scala.util.Try(%(exe)(pwd))
  }
}
