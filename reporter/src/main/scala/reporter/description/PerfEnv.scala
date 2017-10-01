package reporter.description

/**
 * The performance environment.
 *
 * @param machines The list of machines that are use for performance tests.
 */
case class PerfEnv(machines: Seq[Machine], applications: Seq[String], flink: String)