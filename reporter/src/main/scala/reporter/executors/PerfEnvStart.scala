package reporter.executors

import com.decodified.scalassh.SSH
import reporter.description.PerfEnv

/**
 * Command that starts all the services.
 *
 * @param perfEnv Performance environment description.
 * @param runId   Performance test run identifier.
 */
case class PerfEnvStart(perfEnv: PerfEnv, runId: String) extends Command {
  def run(): Unit =
    perfEnv.machines.foreach { machine =>
      SSH(machine.hostname) { client =>
        machine.services.foreach { service =>
          client.exec(s"bash ${service.startScript}")
        }
      }
    }
}
