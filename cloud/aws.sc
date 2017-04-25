import $exec.lib.Ec2
import $exec.lib.CloudFormation
import scala.io._

@main def inventory(stackName: String) = {

  val insts = ec2instances()
  val ids = cfInstanceIds(stackName)
  val filtered = insts.filter(i=> ids.exists(i.id == _))
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
  println("wrote "+inv)

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

