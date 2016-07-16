imageNames in docker := Seq(ImageName(s"sciabarra/alpine-django:1"))

val alpine_node = project.in(file("..")/"alpine-node").enablePlugins(ChartsPlugin)

dockerfile in docker := {
  new Dockerfile {
    from((docker in alpine_node).value.toString)
    runRaw(s"apk add python py-pip sqlite")
    runRaw("""pip install "django>=1.9" django-extensions""")
  }
}
