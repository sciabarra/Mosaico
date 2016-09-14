import sbt.Keys._

version := "0.2-SNAPSHOT"

sbtPlugin := true

name := "sbt-mosaico"

organization := "com.sciabarra"

scalaVersion := "2.10.5"

scalacOptions += "-feature"

libraryDependencies ++= Seq(
  "ch.qos.logback"               %  "logback-classic"      % "1.1.3"   % "test;compile"
  , "org.scalatest"              %% "scalatest"           % "2.2.4"   % "test"
  , "com.typesafe.akka"          %% "akka-testkit"        % "2.3.9"   % "test"
  , "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2"
  , "com.typesafe.akka"          %% "akka-actor"          % "2.3.9"
  , "com.typesafe.akka"          %% "akka-slf4j"          % "2.3.9"
  , "com.typesafe.akka"          %  "akka-stream-experimental_2.10"    % "2.0.3"
  , "com.typesafe.akka"          %  "akka-http-core-experimental_2.10" % "2.0.3"
  , "com.typesafe.akka"          %  "akka-http-experimental_2.10"      % "2.0.3"
  , "com.lihaoyi"                %  s"ammonite_${scalaVersion.value}"  % "0.7.6" //I cannot believe it!
  , "com.lihaoyi"                %  s"ammonite-repl_${scalaVersion.value}"  % "0.7.6" //I cannot believe it!
  )

addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.4.0")

//addSbtPlugin("com.github.alexarchambault" %% "sbt-ammonite" % "0.1.2")
