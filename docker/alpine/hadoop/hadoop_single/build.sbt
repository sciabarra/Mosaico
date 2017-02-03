prpLookup += baseDirectory.value.getParentFile.getParentFile -> "alpine"

imageNames in docker := Seq(ImageName(prp.value("hadoop-single")))

val hadoop = (project in file("..")).enablePlugins(MosaicoDockerPlugin)

dockerfile in docker := {
  val base = baseDirectory.value
  new Dockerfile {
    from((docker in hadoop).value.toString)
    add((base * "*.xml").get, "/usr/hadoop/etc/hadoop/")
    add(base/"run.sh", "/services/hadoop/run")
    runRaw("""|chmod +x /services/hadoop/run ;
              |sed -i -e 's/\${JAVA_HOME}/\/usr\/java/' /usr/hadoop/etc/hadoop/hadoop-env.sh ;
              |sed -i -e 's!ps -p !test -e /proc/!' /usr/hadoop/sbin/hadoop-daemon.sh ;
              |/usr/hadoop/bin/hdfs namenode -format
              |""".stripMargin.replace('\n', ' '))
    workDir("/usr/hadoop")
    expose(50010,50020,50070,50075,50090)
    expose(19888,49707,2122)
    expose(8020,8030,8031,8032,8033,8040,8042,8088)
  }
}
