imageNames in docker := Seq(ImageName(s"sciabarra/alpine-django:1"))

val alpine_s6 = project.in(file("..")/"alpine-s6").enablePlugins(ChartsPlugin)

dockerfile in docker := {
  new Dockerfile {
    from((docker in alpine_s6).value.toString)
    runRaw(s"apk add python py-pip sqlite py-psycopg2")
    runRaw("""pip install --upgrade setuptools "django>=1.9" django-extensions""")
    copy(abuild.toTask(" py-uwsgi py-uwsgi.apk").value, "/tmp/")
    runRaw("apk add --allow-untrusted /tmp/*.apk  && rm /tmp/*.apk")
    runRaw("django-admin startproject hello /home")
    env("DJANGO_APP", "hello")
    env("UWSGI_PROCESSES", "4")
    env("UWSGI_THREADS", "2")

    copy(baseDirectory.value/"run", "/etc/s6/django/run")
    expose(8000,8001)
  }
}
