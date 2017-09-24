import sbt._

object Dependencies {
  object Versions {
    lazy val gatling      = "2.2.3"
    lazy val typesafe     = "1.3.1"
    lazy val docker       = "2.2.1"
    lazy val scalatest    = "3.0.1"
    lazy val ssh          = "0.7.0"
    lazy val kafka        = "0.10.2.1"
    lazy val java8Compat  = "0.3.0"
    lazy val gatlingKafka = "master"
  }

  object Projects {
    lazy val gatlingKafka = RootProject(uri("git://github.com/mnogu/gatling-kafka.git#%s".format(Versions.gatlingKafka)))
  }

  lazy val gatlingHighCharts = "io.gatling.highcharts"  %  "gatling-charts-highcharts" % Versions.gatling
  lazy val gatling           = "io.gatling"             %  "gatling-test-framework"    % Versions.gatling
  lazy val typesafeConfig    = "com.typesafe"           %  "config"                    % Versions.typesafe
  lazy val docker            = "com.spotify"            %  "docker-client"             % Versions.docker
  lazy val ssh               = "com.decodified"         %% "scala-ssh"                 % Versions.ssh
  lazy val kafka             = "org.apache.kafka"       %  "kafka-clients"             % Versions.kafka
  lazy val java8Compat       = "org.scala-lang.modules" %  "scala-java8-compat_2.11"   % Versions.java8Compat

  lazy val scalaTest         = "org.scalatest"          %% "scalatest"                 % Versions.scalatest
}
