
 // parameters
 /// AWS regions
 val region = "us-east-1"
 /// imageId - centos for us-east-1
 val imageId = "ami-ae7bfdb8"
 /// 2 gb each
 val instanceType = "t2.small"
 /// name of your key
 val keyName = "atomic"
 /// the user used by the centos image
 val imageUser = "centos"
 /// location of the cloudformation template
 import ammonite.ops._
 val template = pwd/'conf/"cloudformation.yml"


