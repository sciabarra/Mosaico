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
    add(base/"usr", "/usr")
    runRaw("ln -sf /usr/jdk* /usr/java ; chmod +x /usr/java/bin/*")
    env("JAVA_HOME", "/usr/java")
    env("PATH", "/bin:/sbin:/usr/bin:/usr/sbin:/usr/java/bin")
  }
}
