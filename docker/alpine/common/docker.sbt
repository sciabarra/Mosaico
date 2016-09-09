imageNames in docker := Seq(ImageName(s"sciabarra/alpine-common:1"))

dockerfile in docker := {
  new Dockerfile {
    from("alpine:edge")
    runRaw(s"""
apk update &&
apk add s6 git curl sudo &&
mkdir /etc/s6 &&
ln -sf /bin/s6-svc /bin/svc &&
ln -sf /bin/s6-svstat /bin/svs
    """.replace('\n', ' '))
    cmd("s6-svscan", "/etc/s6")
  }
}
