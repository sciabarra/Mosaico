lazy val root = project.in(file("."))

name := "root"

organization in ThisBuild := "mosaico"

version in ThisBuild := "1"

val alpine = project.in(file("alpine")).enablePlugins(MosaicoPlugin)

addCommandAlias("docker",
  """; python/docker
     ; node/docker
     ; builder/docker
  """.replace('\n', ' '))

imageNames in docker in ThisBuild := Seq(
  ImageName(s"${organization.value}/${name.value}:${version.value}")
)

addCommandAlias("r", "reload")

addCommandAlias("rd", ";reload ;docker")

addCommandAlias("dki", "eval \"docker images !\"")
