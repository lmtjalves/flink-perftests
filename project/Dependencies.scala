import sbt._

object Dependencies {
  object Versions {
    lazy val gatling      = "2.2.3"
    lazy val typesafe     = "1.3.1"
    lazy val ssh          = "0.7.0"
    lazy val kafka        = "0.10.2.1"
    lazy val jcraft       = "1.1.3"
    lazy val logback      = "1.1.2"
    lazy val bouncyCastle = "1.46"
    lazy val gatlingKafka = "master"
    lazy val scalaj       = "2.3.0"
    lazy val json4s       = "3.5.3"
  }

  object Projects {
    lazy val gatlingKafka = RootProject(uri("git://github.com/mnogu/gatling-kafka.git#%s".format(Versions.gatlingKafka)))
  }

  lazy val gatlingHighCharts = "io.gatling.highcharts"  %  "gatling-charts-highcharts" % Versions.gatling
  lazy val gatling           = "io.gatling"             %  "gatling-test-framework"    % Versions.gatling
  lazy val typesafeConfig    = "com.typesafe"           %  "config"                    % Versions.typesafe
  lazy val ssh               = "com.decodified"         %% "scala-ssh"                 % Versions.ssh
  lazy val kafka             = "org.apache.kafka"       %  "kafka-clients"             % Versions.kafka
  lazy val jcraft            = "com.jcraft"             %  "jzlib"                     % Versions.jcraft
  lazy val logback           = "ch.qos.logback"         %  "logback-classic"           % Versions.logback
  lazy val bouncyCastle      = "org.bouncycastle"       %  "bcprov-jdk16"              % Versions.bouncyCastle
  lazy val scalaj            = "org.scalaj"             %% "scalaj-http"               % Versions.scalaj
  lazy val json4s            = "org.json4s"             %% "json4s-native"             % Versions.json4s
}
