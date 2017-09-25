package reporter.description

import com.decodified.scalassh.SshClient


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
) {
  /**
   * Starts the service.
   */
  def start(client: SshClient): Unit = {

  }

  /**
   * Stops the service.
   */
  def stop(client: SshClient): Unit = {

  }

  /**
   * Fetches the logs from the service.
   */
  def fetchLogs(client: SshClient): Unit = {

  }

  /**
   * Deletes the logs in the service.
   */
  def deleteLogs(client: SshClient): Unit = {

  }
}
