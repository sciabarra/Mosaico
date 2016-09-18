prpLookup += baseDirectory.value.getParentFile -> "alpine"

imageNames in docker := Seq(ImageName(prp.value("alpine.jdk")))

val common = (project in file("..")/"common").enablePlugins(MosaicoDockerPlugin)

dockerfile in docker := {
  Def.sequential(
    download.toTask(s" @alpine.jdk.glibc.url glibc.apk"),
    download.toTask(s" @alpine.jdk.url jdk.tgz Cookie: oraclelicense=accept-securebackup-cookie"),
    amm.toTask(s" trimjdk")
  ).value
  val base = baseDirectory.value
  new Dockerfile {
    from((docker in common).value.toString)
    copy(base/"glibc.apk", "/tmp")
    runRaw("apk add --allow-untrusted /tmp/*.apk  && rm /tmp/*.apk")
    add(base/"trimjdk.tar.gz", "/usr")
    runRaw(s"ln -s /usr/jdk${prp.value("alpine.jdk.ver")} /usr/java")
  }
}
