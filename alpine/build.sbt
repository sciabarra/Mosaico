name := "alpine"

imageNames in docker in ThisBuild := Seq(ImageName(s"${organization.value}/${name.value}:${version.value}"))

dockerfile in docker := {
  new Dockerfile {
    from("alpine:edge")
    runRaw(s"""
apk update && apk add s6 git curl sudo && mkdir /etc/s6
    """.replace('\n', ' '))
    cmd("s6-svscan", "/etc/s6")
  }
}

lazy val node = project.in(file("node")).enablePlugins(MosaicoPlugin)

lazy val python = project.in(file("python")).enablePlugins(MosaicoPlugin)

lazy val abuild = project.in(file("abuild")).enablePlugins(MosaicoPlugin)
