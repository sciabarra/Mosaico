name := "mosaico"

version := "0.1"

scalaVersion := "2.11.8"

enablePlugins(MosaicoToplevelPlugin)

addCommandAlias("genDocker", "; genDockerSubs ; reload")

val sc = project in file("scripts")