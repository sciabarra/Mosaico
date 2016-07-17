val plg = project.in(file(".")).dependsOn((file("..")/"sbt").toURI)

addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.4.0")
