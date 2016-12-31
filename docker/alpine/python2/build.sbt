prpLookup += baseDirectory.value.getParentFile -> "alpine"

imageNames in docker := Seq(ImageName(prp.value("python2")))

dockerfile in docker := {
  val apk =
    alpineBuild.toTask(" @abuild py-uwsgi.sh py-uwsgi.apk").value
  new Dockerfile {
    from(prp.value("base"))
    runRaw(s"apk add sqlite python2 py2-pip py-psycopg2 py-pillow")
    copy(apk, "/tmp/")
    runRaw("apk add --allow-untrusted /tmp/*.apk  && rm /tmp/*.apk")
    env("UWSGI_PROCESSES", "4")
    env("UWSGI_THREADS", "2")
    //runRaw("django-admin startproject hello /home")
    //copy(baseDirectory.value / "run.sh", "/services/django/run")
    //runRaw(s"""pip install --upgrade setuptools "django==${prp.value("django.ver")}" django-extensions""")
    //env("DJANGO_APP", "hello")
    //expose(8000, 8001)
  }
}
