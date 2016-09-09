val v = "1"
val ver = "1.8.0_101"
val jdkUrl = "http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-linux-x64.tar.gz"
imageNames in docker := Seq(ImageName(s"sciabarra/alpine-jdk:${ver}-${v}"))

//val common = project.in(file("..")/"common")//.enablePlugins(MosaicoDockerPlugin)

dockerfile in docker := {
  val base = baseDirectory.value
  val oraCookie = "Cookie: oraclelicense=accept-securebackup-cookie"
  val jdk = Some("thing") //download.toTask(s" ${jdkUrl} jdk.tgz ${oraCookie}").value
  new Dockerfile {
    from((docker in common).value.toString)
    add(jdk.get, file("/usr"))
    runRaw(s"ls -s /usr/${}")
  }
}
