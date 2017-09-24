package reporter.description

case class Machine(
  hostname:    String,
  startScript: String,
  stopScript:  String,
  logs:        Option[String]
) {
  def start(): Unit = {

  }

  def stop(): Unit = {

  }

  def fetchLogs(): Unit = {

  }

  def deleteLogs(): Unit = {

  }
}
