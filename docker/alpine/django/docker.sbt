alpineBuildImage := Some("sciabarra/alpine-abuild:1")
val abuild = (project in file("..")/"abuild").enablePlugins(MosaicoDockerPlugin)
val common = (project in file("..")/"common").enablePlugins(MosaicoDockerPlugin)

imageNames in docker := Seq(ImageName(s"sciabarra/alpine-django:1.9.7-1"))

dockerfile in docker := {
  (docker in abuild).value // ensure the build image is built
  new Dockerfile {
    from((docker in common).value.toString)
    runRaw(s"apk add sqlite python2 py2-pip py-psycopg2")
    runRaw("""pip install --upgrade setuptools "django==1.9.7" django-extensions""")
    copy(alpineBuild.toTask(" py-uwsgi.sh py-uwsgi.apk").value, "/tmp/")
    copy(alpineBuild.toTask(" py-uwsgi.sh py-uwsgi.apk").value, "/tmp/")
    runRaw("apk add --allow-untrusted /tmp/*.apk  && rm /tmp/*.apk")
    runRaw("django-admin startproject hello /home")
    env("DJANGO_APP", "hello")
    env("UWSGI_PROCESSES", "4")
    env("UWSGI_THREADS", "2")
    copy(baseDirectory.value/"run.sh", "/services/django/run")
    expose(8000,8001)
  }
}
