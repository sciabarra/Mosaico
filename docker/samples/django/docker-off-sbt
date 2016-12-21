prpLookup += baseDirectory.value.getParentFile -> "alpine"

val common = (project in file("..")/"common").enablePlugins(MosaicoDockerPlugin)

imageNames in docker := Seq(ImageName(prp.value("alpine.nginx")))

dockerfile in docker := {
  val base = baseDirectory.value
  new Dockerfile {
    from((docker in common).value.toString)
    env("DJANGO_HOST", "django.loc")
    runRaw(s"""|apk add nginx ;
               |mkdir -p /run/nginx /home/static /home/media ;
               |""".stripMargin.replace('\n',' '))
    copy(base/"run.sh", "/services/nginx/run")
    copy(base/"django.conf.tpl", "/etc/nginx/django.conf.tpl")
  }
}
