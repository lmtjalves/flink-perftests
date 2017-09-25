package reporter

import com.typesafe.config.ConfigFactory
import reporter.config.ReporterConfig._
import reporter.description.PerfEnv

/**
 * Start the reporter application as follows:
 *
 *    reporters run-id [start|stop]
 *
 * Where:
 *  run-id  The id of the performance test, will be used for the name of the logs folder.
 *  start   Starts the environment.
 *  stop    Stops the environment.
 */
object Reporter extends App {
  //val runId   = args(0)
  //val command = args(1)

  val config = ConfigFactory.load("reporter.conf")
  val perfEnv = config.read[PerfEnv]
  println(perfEnv)
}
