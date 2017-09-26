package reporter.description

/**
 * A service in a machine.
 *
 * @param startScript Start script of the service.
 * @param stopScript  Stop script of the service.
 * @param logsDir     Optional logs directory to be collected for analysis.
 */
case class Service(
  startScript: String,
  stopScript:  String,
  logsDir:     Option[String]
)
