name := "mosaico"

version := "0.1"

scalaVersion := "2.11.8"

// https://mvnrepository.com/artifact/org.apache.commons/commons-compress
libraryDependencies += "org.apache.commons" % "commons-compress" % "1.5"

enablePlugins(MosaicoGeneratorPlugin,MosaicoConfigPlugin)

prpPrefixes := Seq("docker")

