prpLookup += baseDirectory.value.getParentFile -> "alpine"

val abuild = (project in file("..") / "abuild").enablePlugins(MosaicoDockerPlugin)

val common = (project in file("..") / "common").enablePlugins(MosaicoDockerPlugin)

imageNames in docker := Seq(ImageName(prp.value("alpine.django")))

dockerfile in docker := {
  val apk = Def.sequential(
    docker in abuild, // ensure the build image is built
    alpineBuild.toTask(" @alpine.abuild py-uwsgi.sh py-uwsgi.apk")
  ).value
  new Dockerfile {
    from((docker in common).value.toString)
    runRaw(s"apk add sqlite python2 py2-pip py-psycopg2")
    runRaw(s"""pip install --upgrade setuptools "django==${prp.value("alpine.django.ver")}" django-extensions""")
    copy(apk, "/tmp/")
    runRaw("apk add --allow-untrusted /tmp/*.apk  && rm /tmp/*.apk")
    runRaw("django-admin startproject hello /home")
    env("DJANGO_APP", "hello")
    env("UWSGI_PROCESSES", "4")
    env("UWSGI_THREADS", "2")
    copy(baseDirectory.value / "run.sh", "/services/django/run")
    expose(8000, 8001)
  }
}
