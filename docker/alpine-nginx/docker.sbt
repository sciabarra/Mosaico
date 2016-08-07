imageNames in docker := Seq(ImageName(s"sciabarra/alpine-nginx:1"))

val alpine_s6 = project.in(file("..")/"alpine-s6").enablePlugins(MosaicoDockerPlugin)

//|sed -i  -e '1s/^/daemon off;\n/' /etc/nginx/nginx.conf ;

dockerfile in docker := {
  val base = baseDirectory.value
  new Dockerfile {
    from((docker in alpine_s6).value.toString)
    env("DJANGO_HOST", "django.loc")
    runRaw(s"""|apk add nginx ;
               |mkdir -p /run/nginx /home/static /home/media ;
               |""".stripMargin.replace('\n',' '))
    copy(base/"run", "/etc/s6/nginx/run")
    copy(base/"django.conf.tpl", "/etc/nginx/django.conf.tpl")
  }
}
