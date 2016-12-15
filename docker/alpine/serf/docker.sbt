prpLookup += baseDirectory.value.getParentFile -> "alpine"

imageNames in docker := Seq(ImageName(prp.value("alpine.serf")))

val common = (project in file("..") / "common").enablePlugins(MosaicoDockerPlugin)

dockerfile in docker := {
  Def.sequential(
    download.toTask(s" @alpine.serf.url serf.zip"),
    unzip.toTask(s" serf.zip")
  ).value
  val base = baseDirectory.value
  new Dockerfile {
    from((docker in common).value.toString)
    copy(base / "target" / "serf", "/usr/bin")
    runRaw(
      s"""|apk update && apk add sudo bash augeas ;
          |mkdir /etc/serf ;
          |chmod +x /usr/bin/serf ;
          |""".stripMargin.replace('\n', ' '))
    copy(baseDirectory.value / "run.sh", "/services/serf/run")
    expose(7946)
    cmd("/usr/bin/svscan", "/services/")
  }
}
