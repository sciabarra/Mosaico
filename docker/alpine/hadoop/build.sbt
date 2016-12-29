prpLookup += baseDirectory.value.getParentFile -> "alpine"

imageNames in docker := Seq(ImageName(prp.value("hadoop")))

dockerfile in docker := {
  download.toTask(s" @hadoop.url hadoop.tgz").value
  val base = baseDirectory.value
  new Dockerfile {
    from(prp.value("jdk8"))
    add(base/"hadoop.tgz", "/usr")
    runRaw("ln -sf /usr/hadoop-* /usr/hadoop ; chmod +x /usr/hadoop/bin/* /usr/hadoop/sbin/*")
    env("HADOOP_PREFIX", "/usr/hadoop")
  }
}

//val hadoop_namenode = project

//val hadoop_datanode = project

//val hadoop_secnode = project

//val hadoop_resman = project

//val hadoop_proxy = project

//val hadoop_history = project

//val allHadoopImages = project
//   .aggregate(hadoop_namenode,hadoop_datanode,hadoop_secnode,
//              hadoop_resman, hadoop_proxy, hadoop_history)
