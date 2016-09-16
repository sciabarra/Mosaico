val v = "1"
val ver = "1.8.0_101"
val jdkUrl = "http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-linux-x64.tar.gz"
val oraCookie = "Cookie: oraclelicense=accept-securebackup-cookie"
val glibcUrl = "https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.23-r3/glibc-2.23-r3.apk"
val common = (project in file("..")/"common").enablePlugins(MosaicoDockerPlugin)

imageNames in docker := Seq(ImageName(s"sciabarra/alpine-jdk:${ver}-${v}"))

dockerfile in docker := {
  val base = baseDirectory.value
  Def.sequential(
    download.toTask(s" ${glibcUrl} glibc.apk"),
    download.toTask(s" ${jdkUrl} jdk.tgz ${oraCookie}"),
    amm.toTask(s" trimjdk")
  ).value
  new Dockerfile {
    from((docker in common).value.toString)
    copy(base/"glibc.apk", "/tmp")
    runRaw("apk add --allow-untrusted /tmp/*.apk  && rm /tmp/*.apk")
    add(base/"trimjdk.tar.gz", "/usr")
    runRaw(s"ln -s /usr/jdk${ver} /usr/java")
  }
}
