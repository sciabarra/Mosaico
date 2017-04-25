import $ivy.`com.amazonaws:aws-java-sdk:1.11.22`
import $exec.Params

import ammonite.ops._
import ammonite.ops.ImplicitWd._
import com.amazonaws.regions.{Region=>Ec2Region,Regions=>Ec2Regions}
import com.amazonaws.services.ec2._
import com.amazonaws.services.ec2.model._
import collection.JavaConverters._

val ec2 = new AmazonEC2Client()
val regions = Ec2Regions.fromName(region)
ec2.setRegion(Ec2Region.getRegion(regions))

case class Inst(id: String,
                name: Option[String],
                state: String,
                privateIp: String,
                publicIp: String)

def ec2instName(inst: Instance) = inst.getTags.asScala.filter(_.getKey=="Name").map(_.getValue).headOption

def ec2instances() = for {
  reservation <- ec2.describeInstances.getReservations.asScala
  instance <- reservation.getInstances.asScala
  if instance.getState.getName == "running"
} yield {
  Inst(
    id= instance.getInstanceId,
    state = instance.getState.getName,
    name= ec2instName(instance),
    privateIp= instance.getPrivateIpAddress,
    publicIp= instance.getPublicIpAddress
  )
}

def ec2ipsByName(names: String) = {
  val name1 = ","+names+","
  ec2instances().filter(x => name1.indexOf(s",${x.name.getOrElse("")},") != -1 ).
    map(_.publicIp)
}
