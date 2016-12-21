prpLookup += baseDirectory.value.getParentFile -> "alpine"

imageNames in docker := Seq(ImageName(prp.value("serf")))

dockerfile in docker := {
  Def.sequential(
    download.toTask(s" @serf.url serf.zip"),
    unzip.toTask(s" serf.zip target")
  ).value
  val basedir = baseDirectory.value
  new Dockerfile {
    from(prp.value("base"))
    add(basedir / "target" / "serf", "/usr/bin/")
    runRaw(
      s"""|mkdir /etc/serf ;
          |chmod +x /usr/bin/serf ;
          |""".stripMargin.replace('\n', ' '))
    add(basedir / "run.sh", "/services/serf/run")
    expose(7946)
    cmd("/usr/bin/svscan", "/services/")
  }
}
