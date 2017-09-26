package reporter.description

import com.decodified.scalassh.SSH

/**
 * A machine in the performance tests environment.
 *
 * @param hostname The machine hostname so that we can connect to it.
 * @param services The services to be used on the machine.
 */
case class Machine(
  hostname: String,
  services: Seq[Service]
)
