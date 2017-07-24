import ammonite.ops._

def jenkinsPasswd(stackName: String) {
  println("*** jenkins password ***")
  exec("cat /home/centos/.jenkins/secrets/initialAdminPassword")(stackName, "master")
}
