package reporter.description

/**
 * A machine in the performance tests environment.
 *
 * @param hostname The machine hostname so that we can connect to it.
 * @param services The services to be used on the machine.
 */
case class Machine(
  hostname: String,
  services: Seq[Service]
) {
  /**
   * Starts the services.
   */
  def start(): Unit = {

  }

  /**
   * Stops the services.
   */
  def stop(): Unit = {

  }

  /**
   * Fetches the logs from the services.
   */
  def fetchLogs(): Unit = {

  }

  /**
   * Deletes the logs in the services.
   */
  def deleteLogs(): Unit = {

  }
}
