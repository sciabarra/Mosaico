imageNames in docker := Seq(ImageName(s"sciabarra/alpine-django:1.9.7-1"))

dockerfile in docker := {
  new Dockerfile {
    from((docker in common).value.toString)
    runRaw(s"apk add sqlite python2 py2-pip py-psycopg2")
    runRaw("""pip install --upgrade setuptools "django==1.9.7" django-extensions""")
    copy(alpBuild.toTask(" py-uwsgi py-uwsgi.apk").value, "/tmp/")
    runRaw("apk add --allow-untrusted /tmp/*.apk  && rm /tmp/*.apk")
    runRaw("django-admin startproject hello /home")
    env("DJANGO_APP", "hello")
    env("UWSGI_PROCESSES", "4")
    env("UWSGI_THREADS", "2")
    copy(baseDirectory.value/"run", "/services/django/run")
    expose(8000,8001)
  }
}
