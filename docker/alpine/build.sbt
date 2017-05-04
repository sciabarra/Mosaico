lazy val abuild = project
  .enablePlugins(MosaicoDockerPlugin)

lazy val base = project
  .enablePlugins(MosaicoDockerPlugin)

lazy val nginx = project
  .enablePlugins(MosaicoDockerPlugin)

lazy val jdk8 = project
  .enablePlugins(MosaicoDockerPlugin)

lazy val sbt13 = project
    .enablePlugins(MosaicoDockerPlugin)

lazy val python2 = project
  .enablePlugins(MosaicoDockerPlugin)

lazy val hadoop = project
  .enablePlugins(MosaicoDockerPlugin)

lazy val spark = project
  .enablePlugins(MosaicoDockerPlugin)

lazy val cassandra = project
  .enablePlugins(MosaicoDockerPlugin)

lazy val zeppelin = project
  .enablePlugins(MosaicoDockerPlugin)

lazy val all = (project in file(".")).
  aggregate(abuild, base, nginx, jdk8, python2, hadoop, spark, cassandra, zeppelin)
