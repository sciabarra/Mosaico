name := "charts"

organization := "com.sciabarra"

scalacOptions += "-feature"

lazy val root = project.in(file("."))

val sbt_plugin = project.in(file("plugin"))

val alpine_s6 = project.in(file("alpine-s6")).enablePlugins(ChartsPlugin)

val alpine_abuild = project.in(file("alpine-abuild")).enablePlugins(ChartsPlugin)

val alpine_node = project.in(file("alpine-node")).enablePlugins(ChartsPlugin)

val alpine_django = project.in(file("alpine-django")).enablePlugins(ChartsPlugin)
