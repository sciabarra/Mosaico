name := "mosaico"

version := "0.3"

scalaVersion := "2.11.8"

enablePlugins(SupportPlugin)

lazy val alpine = project in file("docker") / "alpine"

lazy val cloud = project