import ammonite.ops._
import collection.JavaConverters._
import $ivy.`com.amazonaws:aws-java-sdk:1.11.22`
import $exec.Params

// cloudformation client
import scala.io._
import com.amazonaws.services._
import com.amazonaws.services.cloudformation.model._

val cf = new cloudformation.AmazonCloudFormationClient

// create
def cfCreateRequest(stackName: String) = {
  import com.amazonaws.services.cloudformation._
  import com.amazonaws.services.cloudformation.model._
  val cfr = new CreateStackRequest()
  cfr.setStackName(stackName)
  val params =  Seq(
    new Parameter().withParameterKey("KeyName").withParameterValue(keyName),
    new Parameter().withParameterKey("InstanceType").withParameterValue(instanceType),
    new Parameter().withParameterKey("ImageId").withParameterValue(imageId)
  )
  cfr.setParameters(params.asJava)
  cfr
}

def cfDeleteRequest(stackName: String) = {
  new DeleteStackRequest().withStackName(stackName)
}

def cfResources(stackName: String) = {
  val res = cf.describeStackResources(
    new DescribeStackResourcesRequest()
      .withStackName(stackName))
  res.getStackResources.asScala
    .map(x => x.getResourceType -> x.getPhysicalResourceId)
}

def cfInstanceIds(stackName: String) =
  cfResources(stackName).filter(_._1=="AWS::EC2::Instance").map(_._2)

def cfStack(stackName: String) = {
  cf.describeStacks
    .getStacks.asScala
    .filter( _.getStackName == stackName)
    .headOption
}

def cfStatusLoop(stackName: String):Unit = {
  val st = cfStack(stackName).map(_.getStackStatus)
  if(st.nonEmpty && st.get.endsWith("_IN_PROGRESS")) {
    println(st.get)
    Thread.sleep(1000)
    cfStatusLoop(stackName)
  }
}

