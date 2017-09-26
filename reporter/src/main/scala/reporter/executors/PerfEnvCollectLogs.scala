package reporter.executors

import com.decodified.scalassh.SSH
import reporter.description.PerfEnv

/**
 * Command that collects logs from all the services and stored it locally in a tgz.
 *
 * @param logsPath The path to the folder where all the logs should be executed.
 * @param perfEnv  Performance environment description.
 * @param runId    Performance test run identifier.
 */
case class PerfEnvCollectLogs(logsPath: String, perfEnv: PerfEnv, runId: String) extends Command {
  def run(): Unit =
    perfEnv.machines.foreach { machine =>
      SSH(machine.hostname) { client =>
        machine.services.foreach { service =>
          client.exec(s"tar -zcvf /tmp/${runId}.tgz ${service.logsDir}")
          client.download(s"/tmp/${runId}.tgz", s"${logsPath}")
        }
      }
    }
}
