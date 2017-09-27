package reporter.executors

import com.decodified.scalassh.SSH
import reporter.description.PerfEnv
import sys.process._

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
          service.logsDir.foreach { logs =>
            val logsDir = logs.split("/").takeRight(1).head

            // We need to guarantee that the folder actually exists
            s"mkdir ${logsPath}".!

            // Compress the logs and download them
            client.exec(s"cd ${logs}/.. && tar -zcvf /tmp/${runId}.tgz ${logsDir}")
            client.download(s"/tmp/${runId}.tgz", s"${logsPath}/${runId}.tgz")

            // Remove the temporary tgz on the remote machine
            client.exec(s"rm -rf /tmp/${runId}.tgz")
          }
        }
      }
    }
}
