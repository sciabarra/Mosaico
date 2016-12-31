lazy val abuild = project
  .enablePlugins(MosaicoDockerPlugin)

lazy val base = project
  .enablePlugins(MosaicoDockerPlugin)
  .aggregate(abuild)

lazy val nginx = project
  .enablePlugins(MosaicoDockerPlugin)
  .aggregate(base)

lazy val jdk8 = project
  .enablePlugins(MosaicoDockerPlugin)
  .aggregate(base)

lazy val python2 = project
  .enablePlugins(MosaicoDockerPlugin)
  .aggregate(base, abuild)

lazy val hadoop = project
  .enablePlugins(MosaicoDockerPlugin)
  .aggregate(jdk8)

lazy val spark = project
  .enablePlugins(MosaicoDockerPlugin)
  .aggregate(hadoop)

lazy val allImages = project
  .aggregate(abuild, base, nginx, jdk8, python2, hadoop, spark)
