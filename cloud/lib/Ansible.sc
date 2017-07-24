import ammonite.ops._

def ansible(stackName: String, file:String="site.yml"): Unit =
  %("ansible-playbook", "-i", s"conf/${stackName}.inv",  s"ansible/${file}")(pwd)
