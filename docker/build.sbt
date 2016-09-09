name := "sbt-plugin"

organization := "com.mosaico"

scalacOptions += "-feature"

/*
lazy val root = project.in(file(".")).enablePlugins(MosaicoDockerPlugin)

val alpine_s6 = project.in(file("alpine-s6")).enablePlugins(MosaicoDockerPlugin)

val alpine_abuild = project.in(file("alpine-abuild")).enablePlugins(MosaicoDockerPlugin)

val alpine_nginx = project.in(file("alpine-nginx")).enablePlugins(MosaicoDockerPlugin)

val alpine_django = project.in(file("alpine-django")).enablePlugins(MosaicoDockerPlugin)

val alpine_django_websocket = project.in(file("alpine-django-websocket")).enablePlugins(MosaicoDockerPlugin)

val alpine_jdk = project.in(file("alpine-jdk")).enablePlugins(MosaicoDockerPlugin)
*/
