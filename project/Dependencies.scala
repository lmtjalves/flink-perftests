import sbt._

object Dependencies {
  object Versions {
    lazy val gatling   = "2.2.2"
    lazy val typesafe  = "1.3.1"
    lazy val docker    = "2.2.1"
    lazy val scalatest = "3.0.1"
    lazy val ssh       = "0.7.0"
  }

  lazy val gatlingHighCharts = "io.gatling.highcharts" %  "gatling-charts-highcharts" % Versions.gatling
  lazy val gatling           = "io.gatling"            %  "gatling-test-framework"    % Versions.gatling
  lazy val typesafeConfig    = "com.typesafe"          %  "config"                    % Versions.typesafe
  lazy val docker            = "com.spotify"           %  "docker-client"             % Versions.docker
  lazy val ssh               = "com.decodified"        %% "scala-ssh"                 % Versions.ssh

  lazy val scalaTest         = "org.scalatest"         %% "scalatest"                 % Versions.scalatest  % Test
}
