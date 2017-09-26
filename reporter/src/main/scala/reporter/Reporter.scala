package reporter

import com.typesafe.config.ConfigFactory
import reporter.config.ReporterConfig._
import reporter.description.PerfEnv
import reporter.executors._

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
  val usage =
    """
      |Start the reporter application as follows:
      |
      |    reporters runId logsDir [start|stop]
      |
      |Where:
      |  runId   The id of the performance test, will be used for the name of the logs folder.
      |  logsDir Path where to store the logs.
      |  start   Starts the environment.
      |  stop    Stops the environment.
    """.stripMargin

  def getArgs: Option[(String, String, String)] = {
    if(args.size != 3) {
      println(usage)
      None
    } else {
      Some((args(0), args(1), args(2)))
    }
  }

  for(
    (runId, logsDir, command) <- getArgs
  ) {
    val config  = ConfigFactory.load("reporter.conf")
    val perfEnv = config.read[PerfEnv].get

    command match {
      case "start" =>
        PerfEnvStop(perfEnv, runId).run()
        PerfEnvDeleteLogs(perfEnv, runId).run()
        PerfEnvStart(perfEnv, runId).run()
      case "stop" =>
        PerfEnvStop(perfEnv, runId).run()
        PerfEnvCollectLogs(logsDir, perfEnv, runId).run()
      case _ =>
        println(usage)
    }
  }
}