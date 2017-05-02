import $exec.Params
import $exec.Ssh

def docker(arg: String)(implicit hosts: Tuple2[String,String]) {
  exec(s"docker ${arg}")(hosts._1, hosts._2)
}
