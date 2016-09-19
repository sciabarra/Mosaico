val sbtMosaicoVer = "0.2"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.4.0")

addSbtPlugin("com.sciabarra" % "sbt-mosaico" % sbtMosaicoVer)

addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.5")

scalacOptions += "-deprecation"
