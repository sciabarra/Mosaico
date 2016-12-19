lazy val abuild = (project in file("./docker/alpine/abuild")).enablePlugins(MosaicoDockerPlugin,MosaicoAmmonitePlugin)

lazy val base = (project in file("./docker/alpine/base")).enablePlugins(MosaicoDockerPlugin,MosaicoAmmonitePlugin)

lazy val django = (project in file("./docker/alpine/django")).enablePlugins(MosaicoDockerPlugin,MosaicoAmmonitePlugin)

lazy val jdk8 = (project in file("./docker/alpine/jdk8")).enablePlugins(MosaicoDockerPlugin,MosaicoAmmonitePlugin)

lazy val nginx = (project in file("./docker/alpine/nginx")).enablePlugins(MosaicoDockerPlugin,MosaicoAmmonitePlugin)

lazy val serf = (project in file("./docker/alpine/serf")).enablePlugins(MosaicoDockerPlugin,MosaicoAmmonitePlugin)

addCommandAlias("dockers", "; abuild/docker ; base/docker ; django/docker ; jdk8/docker ; nginx/docker ; serf/docker")
    