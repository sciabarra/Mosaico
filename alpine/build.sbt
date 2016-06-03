name := "alpine"

dockerfile in docker := {
  new Dockerfile {
    from("alpine:edge")
    runRaw(s"""
apk update && apk add s6 && mkdir /etc/s6
    """.replace('\n', ' '))
    cmd("s6-svscan", "/etc/s6")
  }
}

lazy val node = project.in(file("node")).enablePlugins(MosaicoPlugin)

lazy val python = project.in(file("python")).enablePlugins(MosaicoPlugin)

