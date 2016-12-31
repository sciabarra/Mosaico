prpLookup += baseDirectory.value.getParentFile.getParentFile -> "alpine"

imageNames in docker := Seq(ImageName(prp.value("hadoop.namenode")))

dockerfile in docker := {
  new Dockerfile {
    from(prp.value("hadoop"))
    add("run.sh", "/service/namenode/run")
  }
}
