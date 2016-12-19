prpLookup += baseDirectory.value.getParentFile -> "alpine"

imageNames in docker := Seq(ImageName(prp.value("jdk8")))

val base = (project in file("..")/"base").enablePlugins(MosaicoDockerPlugin)

dockerfile in docker := {
  Def.sequential(
    download.toTask(s" @jdk8.glibc.url glibc.apk"),
    download.toTask(s" @jdk8.url jdk.tgz Cookie: oraclelicense=accept-securebackup-cookie"),
    amm.toTask(s" trimjdk")
  ).value
  val basedir = baseDirectory.value
  new Dockerfile {
    from((docker in base).value.toString)
    add(basedir/"glibc.apk", "/tmp")
    runRaw("apk add --allow-untrusted /tmp/*.apk  && rm /tmp/*.apk")
    add(basedir/"usr", "/usr")
    runRaw("ln -sf /usr/jdk* /usr/java ; chmod +x /usr/java/bin/*")
    env("JAVA_HOME", "/usr/java")
    env("PATH", "/bin:/sbin:/usr/bin:/usr/sbin:/usr/java/bin")
  }
}
