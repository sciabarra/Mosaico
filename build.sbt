lazy val root = project.in(file("."))

name := "root"

organization in ThisBuild := "mosaico"

version in ThisBuild := "1"

val alpine = project.in(file("alpine")).enablePlugins(MosaicoPlugin)

addCommandAlias("docker",
  """; python/docker
     ; node/docker
     ; abuild/docker
  """.replace('\n', ' '))

addCommandAlias("r", "reload")

addCommandAlias("rd", ";reload ;docker")

//addCommandAlias("dki", "eval \"docker images !\"")
scalacOptions += "-feature"

val plugin = project.in(file("plugin"))