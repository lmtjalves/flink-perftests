import Dependencies._

name := "flink-perf-tests"

// used like the groupId in maven
organization in ThisBuild := "org.lalves"

// all sub projects have the same version
version in ThisBuild := "0.1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.8"

lazy val ignitor = project
  .settings(libraryDependencies ++= Seq(
    ssh,
    typesafeConfig,

    scalaTest
  ))

lazy val injector = project
  .settings(libraryDependencies ++= Seq(
    gatling,
    gatlingHighCharts,
    typesafeConfig,

    scalaTest
  ))

lazy val reporter = project
  .settings(libraryDependencies ++= Seq(
    ssh,
    docker,
    typesafeConfig,

    scalaTest
  ))
