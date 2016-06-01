name := "mosaico"

organization := "com.mosaico"

version := "0.1"

val base = project.in(file("base")).
  enablePlugins(DockerPlugin,MosaicoPlugin)
