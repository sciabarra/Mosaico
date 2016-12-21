prpLookup += baseDirectory.value.getParentFile -> "alpine"

imageNames in docker := Seq(ImageName(prp.value("base")))

dockerfile in docker := {
  val apk = alpineBuild.toTask(
     " @abuild daemontools.sh daemontools.apk").value
  new Dockerfile {
    from("alpine:edge")
    copy(apk, "/tmp/")
    runRaw(s"""apk update && apk add git curl sudo bash augeas""")
    runRaw("apk add --allow-untrusted /tmp/*.apk  && rm /tmp/*.apk")
    cmd("/usr/bin/svscan", "/services/")
  }
}
