package reporter.description

/**
 * The performance environment.
 *
 * @param machines The list of machines that are use for performance tests.
 */
case class PerfEnv(machines: Seq[Machine]) {
  /**
   * Starts the machines.
   */
  def start(): Unit = machines.foreach(_.start())

  /**
   * Stops the machines.
   */
  def stop(): Unit = machines.foreach(_.stop())

  /**
   * Fetches the logs from the machines.
   */
  def fetchLogs(): Unit = machines.foreach(_.fetchLogs())

  /**
   * Deletes the logs in the machines.
   */
  def deleteLogs(): Unit = machines.foreach(_.deleteLogs())
}
