name := """coursier-test"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  cache,
  javaJdbc,
  "org.apache.avro"               % "avro-ipc"                  % "1.7.7"
)
