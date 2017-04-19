import $ivy.`com.amazonaws:aws-java-sdk:1.11.22`
import ammonite.ops._
import ammonite.ops.ImplicitWd._
import com.amazonaws.regions.{Region,Regions}
import com.amazonaws.services.ec2._
import com.amazonaws.services.ec2.model._
import collection.JavaConverters._

val region = "us-east-1"
val ec2 = new AmazonEC2Client()
val regions = Regions.fromName(region)
ec2.setRegion(Region.getRegion(regions))

case class Inst(id: String, name: Option[String], state: String, privateIp: String, publicIp: String)

def instName(inst: Instance) = inst.getTags.asScala.filter(_.getKey=="Name").map(_.getValue).headOption

val instances = for {
  reservation <- ec2.describeInstances.getReservations.asScala
  instance <- reservation.getInstances.asScala
} yield {
  Inst(
    id= instance.getInstanceId,
    state = instance.getState.getName,
    name= instName(instance),
    privateIp= instance.getPrivateIpAddress,
    publicIp= instance.getPublicIpAddress
  )
}


println(instances)

val master = instances.filter(_.name.getOrElse("") == "master")
val nodes = instances.filter(_.name.getOrElse("") != "master")

val ids = instances.filter(_.state == "running").map(_.id)
val ips = instances.filter(_.state == "running").map(i => i.privateIp -> i.publicIp)

if(master.nonEmpty) {

  val inventory = Seq(
    "[all:vars]",
    s"kube_master_host=${master.head.publicIp}",
    s"kube_master=${master.head.privateIp}",
    "[aws]"
  ) ++ ips.map(x => s"${x._2} local_ip=${x._1}") ++
    Seq("[masters]", s"${master.head.publicIp} local_ip=${master.head.privateIp}") ++
    Seq("[nodes]") ++
    nodes.map(t => s"${t.publicIp} local_ip=${t.privateIp}")

  val inv = pwd / 'aws / "inventory"
  rm ! inv
  write(inv, inventory.mkString("\n"))
  println("wrote "+inv)

  val kubecfg =
    s"""
apiVersion: v1
clusters:
- cluster:
    server: http://${master.head.publicIp}:8080
  name: aws
contexts:
- context:
    cluster: aws
    user: ""
  name: aws
current-context: aws
"""

  val cfg = pwd / 'aws / "kubeconfig"
  rm ! cfg
  write(cfg, kubecfg)
  println("wrote "+cfg)

  %.apply("ansible-playbook", "-i", "../aws/inventory", "site.yml")(pwd/'ansible)

} else {
  println("no master")
}
