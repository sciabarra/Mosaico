imageNames in docker := Seq(ImageName(s"sciabarra/alpine-django-websocket:1"))

val alpine_django = project.in(file("..")/"alpine-django").enablePlugins(ChartsPlugin)

val buildApk = taskKey[Seq[java.io.File]]("apk")

buildApk := {
  val pyGreenlet = abuild.toTask(" py-greenlet py-greenlet.apk").value
  val pyGevent = abuild.toTask(" py-gevent py-gevent.apk").value
  pyGreenlet ++ pyGevent
}

dockerfile in docker := {
  new Dockerfile {
    from((docker in alpine_django).value.toString)
    copy(buildApk.value, "/tmp/")
    runRaw("apk add --allow-untrusted /tmp/*.apk  && rm /tmp/*.apk")
    runRaw("pip install django-websocket-redis")
  }
}
