lazy val abuild = (project in file("./abuild")).enablePlugins(MosaicoDockerPlugin,MosaicoAmmonitePlugin)

lazy val common = (project in file("./common")).enablePlugins(MosaicoDockerPlugin,MosaicoAmmonitePlugin)

addCommandAlias("dockers", "; abuild/docker ; common/docker")
