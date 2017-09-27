package reporter.executors

import com.decodified.scalassh.SSH
import reporter.description.PerfEnv

/**
 * Command that deletes logs from all the services remotely.
 *
 * @param perfEnv Performance environment description.
 * @param runId   Performance test run identifier.
 */
case class PerfEnvDeleteLogs(perfEnv: PerfEnv, runId: String) extends Command {
  def run(): Unit =
    perfEnv.machines.foreach { machine =>
      SSH(machine.hostname) { client =>
        machine.services.foreach { service =>
          service.logsDir.foreach(logs => client.exec(s"rm -rf ${logs}/*"))
        }
      }
    }
}
