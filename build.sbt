name := "mosaico"

version := "0.3"

scalaVersion := "2.11.11"

enablePlugins(SupportPlugin)

lazy val alpine = project in file("docker") / "alpine"

lazy val cloud = project

addCommandAlias("amm", "cloud/amm")
