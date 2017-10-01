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

// --------- Project Injector -----------------

lazy val injector = project
  .settings(commonSettings ++ injectorSettings)
  .enablePlugins(GatlingPlugin)
  .enablePlugins(JavaAppPackaging)
  .dependsOn(Dependencies.Projects.gatlingKafka)

lazy val injectorSettings = Seq(
  mainClass in assembly := Some("injector.Injector"),

  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.first
    case resource => (assemblyMergeStrategy in assembly).value(resource)
  },

  mappings in Universal ++= {
    ((resourceDirectory in Test).value * "*").get.map { f =>
      f -> s"conf/${f.name}"
    }
  },
  mappings in Universal ++= {
    ((packageBin in Test).value).get.map { f =>
      f -> s"lib/${f.name}"
    }
  },

  bashScriptExtraDefines += """addJava "-Dconfig.file=${app_home}/../conf/injector.conf"""",

  libraryDependencies ++= Seq(
    Dependencies.gatling,
    Dependencies.gatlingHighCharts,
    Dependencies.typesafeConfig,
    Dependencies.kafka,
    Dependencies.scalaj,
    Dependencies.json4s
  )
)

// --------- Project Reporter -----------------

lazy val reporter = project
  .settings(commonSettings ++ reporterSettings)
  .enablePlugins(JavaAppPackaging)

lazy val reporterSettings = Seq(
  mainClass in assembly := Some("reporter.Reporter"),

  mappings in Universal ++= {
    ((resourceDirectory in Compile).value * "*").get.map { f =>
      f -> s"conf/${f.name}"
    }
  },

  bashScriptExtraDefines += """addJava "-Dconfig.file=${app_home}/../conf/reporter.conf"""",

  libraryDependencies ++= Seq(
    Dependencies.ssh,
    Dependencies.typesafeConfig,
    Dependencies.jcraft,
    Dependencies.logback,
    Dependencies.bouncyCastle,
    Dependencies.scalaj
  )
)

lazy val distFlinkPerfTests = taskKey[File]("Creates a distributable zip file")
distFlinkPerfTests := {
  val name    = "flink-perftests"
  val distZip = target.value / (name + ".zip")

  // Cleanup target
  IO.delete(distZip)

  val injectorZip = (packageBin in Universal in injector).value
  val reporterZip = (packageBin in Universal in reporter).value
  val shZip       = (packageBin in Universal in sh).value

  IO.withTemporaryDirectory { tmpFolder =>
    // Unzip all the artifacts into the distribution folder
    Seq(injectorZip, reporterZip, shZip).map { a =>
      val files = IO.unzip(a, tmpFolder)
      files.foreach { f =>
        // Move all files from the unziped folder
        val relativePath = tmpFolder.toPath.relativize(f.toPath)
        val toFile = new File(tmpFolder.toString, relativePath.subpath(1, relativePath.getNameCount).toString)
        IO.move(f, toFile)
      }

      // Remove the unziped files
      IO.delete((tmpFolder / a.getName.replace(".zip", "")))
    }

    IO.zip(
      Path.allSubpaths(tmpFolder).map { case (file, path) => file -> s"${name}/${path}" },
      distZip
    )
  }

  distZip
}


