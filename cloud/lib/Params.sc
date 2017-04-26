import ammonite.ops._

// parameters
val region = "us-east-1"
val imageId = "ami-ae7bfdb8" //centos standard
//val imageId = "ami-a50d85b3" // atomic host
val instanceType = "t2.small"
val keyName = "atomic"
val template = pwd/'conf/"cloudformation.yml"
val imageUser = "centos"
