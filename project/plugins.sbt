val sbtMosaicoVer = "0.3-SNAPSHOT"

//addSbtPlugin("com.sciabarra" % "sbt-mosaico" % sbtMosaicoVer)

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.4.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.5")

scalacOptions += "-deprecation"

val root = project.in(file(".")).dependsOn(file("plugin").toURI)

