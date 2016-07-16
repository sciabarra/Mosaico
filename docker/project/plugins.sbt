val me = project.in(file(".")).dependsOn(file("plugin").toURI)

addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.4.0")
