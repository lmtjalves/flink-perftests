import sbtassembly.AssemblyPlugin.autoImport.assemblyMergeStrategy
import com.typesafe.sbt.packager.universal.Archives._


lazy val commonSettings = Seq(
  organization := "org.lalves",
  version      := "0.1.0-SNAPSHOT",
  scalaVersion := "2.11.8",

  // Don't run tests during assembly
  test in assembly := {}
)

// --------- Project sh (scripts) ------------------

lazy val sh = project
  .settings(commonSettings)
  .enablePlugins(JavaAppPackaging)

// --------- Project Ignitor ------------------

lazy val ignitor = project
  .settings(commonSettings ++ ignitorSettings)
  .enablePlugins(JavaAppPackaging)

lazy val ignitorSettings = Seq(
  mainClass in assembly := Some("example.Hello"),

  libraryDependencies ++= Seq(
    Dependencies.ssh,
    Dependencies.typesafeConfig,
    Dependencies.scalaTest % Test
  )
)

// --------- Project Injector -----------------

lazy val injector = project
  .settings(commonSettings ++ injectorSettings)
  .enablePlugins(GatlingPlugin)
  .enablePlugins(JavaAppPackaging)

lazy val injectorSettings = Seq(
  mainClass in assembly := Some("example.Hello"),

  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.first
    case resource => (assemblyMergeStrategy in assembly).value(resource)
  },

  libraryDependencies ++= Seq(
    Dependencies.gatling,
    Dependencies.gatlingHighCharts,
    Dependencies.typesafeConfig,
    Dependencies.scalaTest % Test
  )
)

// --------- Project Reporter -----------------

lazy val reporter = project
  .settings(commonSettings ++ reporterSettings)
  .enablePlugins(JavaAppPackaging)

lazy val reporterSettings = Seq(
  mainClass in assembly := Some("example.Hello"),

  libraryDependencies ++= Seq(
    Dependencies.ssh,
    Dependencies.docker,
    Dependencies.typesafeConfig,
    Dependencies.scalaTest % Test
  )
)

lazy val distFlinkPerfTests = taskKey[File]("Creates a distributable zip file")
distFlinkPerfTests := {
  val name    = "flink-perftests"
  val distZip = target.value / (name + ".zip")

  // Cleanup target
  IO.delete(distZip)

  val ignitorZip  = (packageBin in Universal in ignitor).value
  val injectorZip = (packageBin in Universal in injector).value
  val reporterZip = (packageBin in Universal in reporter).value
  val shZip       = (packageBin in Universal in sh).value

  IO.withTemporaryDirectory { tmpFile =>
    // Unzip all the artifacts into the distribution folder
    Seq(ignitorZip, injectorZip, reporterZip, shZip).map { a =>
      val files = IO.unzip(a, tmpFile)
      files.foreach { f =>
        // Move all files from the unziped folder
        val relativePath = tmpFile.toPath.relativize(f.toPath)
        val toFile = new File(tmpFile.toString, relativePath.subpath(1, relativePath.getNameCount).toString)
        IO.move(f, toFile)
      }

      // Remove the unziped files
      IO.delete((tmpFile / a.getName.replace(".zip", "")))
    }



    IO.zip(
      Path.allSubpaths(tmpFile).map { case (file, path) => file -> s"${name}/${path}" },
      distZip
    )
  }

  distZip
}


