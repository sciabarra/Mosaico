import $exec.Params
import $exec.Ssh

def dkExec(arg: String)(implicit hosts: Tuple2[String,String]) {
  exec(s"docker ${arg}")(hosts._1, hosts._2)
}

def dkCompose(arg: String)(implicit hosts: Tuple2[String,String]){
  scp(pwd/'compose/arg, arg)(hosts._1, hosts._2)
  val name = arg.split('.').head
  dkExec(s"stack deploy -c ${arg} --with-registry-auth ${name}")(hosts._1, hosts._2)
}
