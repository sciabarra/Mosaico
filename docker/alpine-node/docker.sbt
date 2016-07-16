imageNames in docker := Seq(ImageName(s"sciabarra/alpine-node:1"))

val alpine_s6 = project.in(file("..")/"alpine-s6").enablePlugins(ChartsPlugin)

dockerfile in docker := {
  new Dockerfile {
    from((docker in alpine_s6).value.toString)
    runRaw(s"apk add nodejs")
    runRaw("npm install -g browserify budo")
  }
}
