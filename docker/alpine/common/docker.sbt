imageNames in docker := Seq(ImageName(s"sciabarra/alpine-common:1"))

alpBuildImage := Some("sciabarra/alpine-abuild:1")
val abuild = (project in file("..")/"abuild").enablePlugins(MosaicoDockerPlugin)

dockerfile in docker := {
  (docker in abuild).value // ensure the abuild image is built
  new Dockerfile {
    from("alpine:edge")
    copy(alpBuild.toTask(" daemontools daemontools.apk").value, "/tmp/")
    runRaw(s"""apk update && apk add git curl sudo""")
    runRaw("apk add --allow-untrusted /tmp/*.apk  && rm /tmp/*.apk")
    cmd("/usr/bin/svscan", "/services/")
  }
}
