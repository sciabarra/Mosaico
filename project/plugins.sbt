val me = project.in(file(".")).dependsOn(file("project/plugin").toURI)

addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.4.0")

