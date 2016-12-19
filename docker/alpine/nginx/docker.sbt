prpLookup += baseDirectory.value.getParentFile -> "alpine"

val serf = (project in file("..")/"serf").enablePlugins(MosaicoDockerPlugin)

imageNames in docker := Seq(ImageName(prp.value("nginx")))

dockerfile in docker := {
  val base = baseDirectory.value
  new Dockerfile {
    from((docker in serf).value.toString)
    add(base/"run.sh", "/services/nginx/run")
    runRaw(s"""|apk add nginx ;
               |mkdir -p /run/nginx ;
               |chmod +x /services/nginx/run ;
               |touch /var/log/nginx/access.log ;
               |echo set /files/etc/nginx/nginx.conf/daemon off | augtool -s
               |""".stripMargin.replace('\n',' '))
  }
}
