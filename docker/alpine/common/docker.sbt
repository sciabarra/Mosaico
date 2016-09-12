imageNames in docker := Seq(ImageName(s"sciabarra/alpine-common:1"))

dockerfile in docker := {
  new Dockerfile {
    from("alpine:edge")
    copy(alpBuild.toTask(" daemontools daemontools.apk").value, "/tmp/")
    runRaw(s"""apk update && apk add git curl sudo""")
    copy(alpBuild.toTask(" daemontools daemontools.apk").value, "/tmp/")
    runRaw("apk add --allow-untrusted /tmp/*.apk  && rm /tmp/*.apk")
  }
}
