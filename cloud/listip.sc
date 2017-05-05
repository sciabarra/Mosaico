import $ivy.`com.amazonaws:aws-java-sdk:1.11.22`
import com.amazonaws.services.ec2._
val ec2 = new AmazonEC2Client
val desc = ec2.describeInstances
import collection.JavaConverters._

val ips = for {
  res <- desc.getReservations.asScala
  inst <- res.getInstances.asScala
} yield {
  inst.getPublicIpAddress
}

ips.filter(_ != null).foreach(println)
