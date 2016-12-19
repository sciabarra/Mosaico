prpLookup += baseDirectory.value.getParentFile -> "alpine"

imageNames in docker := Seq(ImageName(prp.value("serf")))

val base = (project in file("..") / "base").enablePlugins(MosaicoDockerPlugin)

dockerfile in docker := {
  Def.sequential(
    download.toTask(s" @serf.url serf.zip"),
    unzip.toTask(s" serf.zip")
  ).value
  val basedir = baseDirectory.value
  new Dockerfile {
    from((docker in base).value.toString)
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
