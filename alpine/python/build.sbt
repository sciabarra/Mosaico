name := "python"

imageNames in docker in ThisBuild := Seq(ImageName(s"${organization.value}/${name.value}:${version.value}"))

lazy val alpine = project.in(file("..")).enablePlugins(MosaicoPlugin)

dockerfile in docker := {
  new Dockerfile {
    from((docker in alpine).value.toString)
    runRaw(s"apk add python py-pip sqlite")
  }
}
