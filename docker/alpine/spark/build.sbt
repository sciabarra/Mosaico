prpLookup += baseDirectory.value.getParentFile -> "alpine"

imageNames in docker := Seq(ImageName(prp.value("spark")))

dockerfile in docker := {
  download.toTask(s" @spark.url spark.tgz").value
  val base = baseDirectory.value
  new Dockerfile {
    from(prp.value("hadoop"))
    add(base/"spark.tgz", "/usr")
    runRaw("ln -sf /usr/spark-* /usr/spark ; chmod +x /usr/spark/bin/* /usr/spark/sbin/*")
  }
}
