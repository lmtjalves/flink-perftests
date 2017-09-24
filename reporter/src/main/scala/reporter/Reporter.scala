package reporter

import com.typesafe.config.ConfigFactory
import reporter.config.ReporterConfig._

object Reporter extends App {
  val runId   = args(0)
  val command = args(1)
  val config = ConfigFactory.load("reporter.conf")

  command match {
    case "start" =>

    case "stop" =>

  }
  val injectorConfig = ConfigFactory.load("injector.conf")
  println(injectorConfig)
}
