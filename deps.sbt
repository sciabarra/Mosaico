lazy val abuild = (project in file("./docker/alpine/abuild")).enablePlugins(MosaicoDockerPlugin,MosaicoAmmonitePlugin)

lazy val common = (project in file("./docker/alpine/common")).enablePlugins(MosaicoDockerPlugin,MosaicoAmmonitePlugin)

lazy val django = (project in file("./docker/alpine/django")).enablePlugins(MosaicoDockerPlugin,MosaicoAmmonitePlugin)

lazy val jdk = (project in file("./docker/alpine/jdk")).enablePlugins(MosaicoDockerPlugin,MosaicoAmmonitePlugin)

lazy val nginx = (project in file("./docker/alpine/nginx")).enablePlugins(MosaicoDockerPlugin,MosaicoAmmonitePlugin)

lazy val serf = (project in file("./docker/alpine/serf")).enablePlugins(MosaicoDockerPlugin,MosaicoAmmonitePlugin)

addCommandAlias("dockers", "; abuild/docker ; common/docker ; django/docker ; jdk/docker ; nginx/docker ; serf/docker")
    