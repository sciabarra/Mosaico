prpLookup += baseDirectory.value.getParentFile -> "alpine"

imageNames in docker := Seq(ImageName(prp.value("sbt13")))

val jdk8 = (project in file("..")/"jdk8").enablePlugins(MosaicoDockerPlugin)

dockerfile in docker := {
  download.toTask(s" @sbt13.url sbt").value
  val baseDir = baseDirectory.value
  new Dockerfile {
    from( (docker in jdk8).value.toString)
    copy(baseDir/"sbt", "/usr/bin/sbt")
    copy(baseDir/"build.properties", "/tmp/project/build.properties")
    copy(baseDir/"plugins.sbt", "/tmp/project/plugins.sbt")
    workDir("/tmp")
    runRaw("touch build.sbt ; chmod +x /usr/bin/sbt ; sbt update")
    workDir("/home")
  }
}
