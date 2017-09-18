// To package the project
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.0")

// For event injection
addSbtPlugin("io.gatling" % "gatling-sbt" % "2.2.0")

// To create fatjar for the sub projects with their dependencies
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.5")
