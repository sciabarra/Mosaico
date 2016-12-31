prpLookup += baseDirectory.value.getParentFile -> "alpine"

imageNames in docker := Seq(ImageName(prp.value("base")))

dockerfile in docker := {
  val basedir = baseDirectory.value
  val apk = Def.sequential(
    download.toTask(s" @base.serf.url serf.zip"),
    unzip.toTask(s" serf.zip target"),
    alpineBuild.toTask(" @abuild daemontools.sh daemontools.apk")
  ).value
  new Dockerfile {
    from("alpine:edge")
    copy(apk, "/tmp/")
    runRaw(
      s"""|apk update ;
          |apk add git curl sudo bash augeas ;
          |apk add --allow-untrusted /tmp/*.apk ;
          |rm /tmp/*.apk ;
          |""".stripMargin.replace('\n', ' '))
    add(basedir / "target" / "serf", "/usr/bin/")
    add(basedir / "serf.sh", "/usr/bin/serf.sh")
    runRaw(
      s"""|mkdir /etc/serf ;
          |chmod +x /usr/bin/serf ;
          |""".stripMargin.replace('\n', ' '))
    expose(7946)
    cmd("/usr/bin/svscan", "/services/")
  }
}
