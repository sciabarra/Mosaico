imageNames in docker := Seq(ImageName(prp.value("alpine.common")))

alpineBuildImage := Some(prp.value("alpine.abuild"))
val abuild = (project in file("..")/"abuild").enablePlugins(MosaicoDockerPlugin)

dockerfile in docker := {
  (docker in abuild).value // ensure the abuild image is built
  val apk = alpineBuild.toTask(" daemontools.sh daemontools.apk").value
  new Dockerfile {
    from("alpine:edge")
    copy(apk, "/tmp/")
    runRaw(s"""apk update && apk add git curl sudo""")
    runRaw("apk add --allow-untrusted /tmp/*.apk  && rm /tmp/*.apk")
    cmd("/usr/bin/svscan", "/services/")
  }
}
