package reporter.executors

import com.decodified.scalassh.SSH
import reporter.description.PerfEnv

/**
 * Command that stops all the services.
 *
 * @param perfEnv Performance environment description.
 * @param runId   Performance test run identifier.
 */
case class PerfEnvStop(perfEnv: PerfEnv, runId: String) {
  def run(): Unit =
    perfEnv.machines.foreach { machine =>
      SSH(machine.hostname) { client =>
        machine.services.foreach { service =>
          client.exec(s"bash ${service.stopScript}")
        }
      }
    }
}
