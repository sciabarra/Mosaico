name := "python-node"

imageNames in docker in ThisBuild := Seq(ImageName(s"${organization.value}/${name.value}:${version.value}"))

lazy val python = project.in(file("..")).enablePlugins(MosaicoPlugin)

dockerfile in docker := {
  new Dockerfile {
    from((docker in python).value.toString)
    runRaw(s"apk add nodejs")
  }
}
