lazy val abuild = project
  .enablePlugins(MosaicoDockerPlugin)

lazy val base = project
  .enablePlugins(MosaicoDockerPlugin)
  .aggregate(abuild)

lazy val serf = project
  .enablePlugins(MosaicoDockerPlugin)
  .aggregate(base)

lazy val nginx = project
  .enablePlugins(MosaicoDockerPlugin)
  .aggregate(serf)

lazy val jdk8 = project
  .enablePlugins(MosaicoDockerPlugin)
  .aggregate(serf)

lazy val python2 = project
  .enablePlugins(MosaicoDockerPlugin)
  .aggregate(serf, abuild)

lazy val allImages = project
  .aggregate(abuild, base, serf, nginx, jdk8, python2)

