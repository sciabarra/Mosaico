name := "node"

lazy val alpine = project.in(file("..")).enablePlugins(MosaicoPlugin)

dockerfile in docker := {
  new Dockerfile {
    from((docker in alpine).value.toString)
    runRaw(s"apk add nodejs")
  }
}
