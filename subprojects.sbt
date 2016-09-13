lazy val abuild = (project in file("./docker/alpine/abuild")).enablePlugins(MosaicoPlugin)

lazy val common = (project in file("./docker/alpine/common")).enablePlugins(MosaicoPlugin)

lazy val django = (project in file("./docker/alpine/django")).enablePlugins(MosaicoPlugin)

lazy val jdk = (project in file("./docker/alpine/jdk")).enablePlugins(MosaicoPlugin)

lazy val nginx = (project in file("./docker/alpine/nginx")).enablePlugins(MosaicoPlugin)

addCommandAlias("dockerBuildAll", "; abuild/docker ; common/docker ; django/docker ; jdk/docker ; nginx/docker")
    