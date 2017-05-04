prpLookup += baseDirectory.value.getParentFile -> "alpine"

imageNames in docker := Seq(ImageName(prp.value("cassandra")))

val jdk8 = (project in file("..") / "jdk8").enablePlugins(MosaicoDockerPlugin)

dockerfile in docker := {
  download.toTask(s" @cassandra.url cassandra.tgz").value
  val base = baseDirectory.value
  new Dockerfile {
    from((docker in jdk8).value.toString)
    add(base / "cassandra.tgz", "/usr")
    add(base / "run.sh", "/services/cassandra/run")
    runRaw("ln -sf /usr/apache-cassandra-* /usr/cassandra ; chmod +x /services/cassandra/run")
  }
}
