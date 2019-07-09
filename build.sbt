import Dependencies._

ThisBuild / name := "Spark1"
ThisBuild / scalaVersion := "2.12.8"
ThisBuild / version := "0.1"

lazy val root = (project in file("."))
  //  .enablePlugins(RootProjectPlugin)
  //  .enablePlugins(CommandLineProjectPlugin)
  .settings(
  name := "Spark1",
  libraryDependencies ++= additional ++ spark ++ testLibs,
    mainClass in Compile := Some("com.zerniuk.SparkApp")
)
