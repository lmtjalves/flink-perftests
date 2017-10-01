package reporter.executors

import java.nio.file.{Files, Paths}

import com.decodified.scalassh.SSH
import reporter.description.PerfEnv

import scala.util.{Failure, Success, Try}
import scalaj.http.{Http, HttpResponse, MultiPart}

/**
 * Command that starts all the services.
 *
 * @param perfEnv Performance environment description.
 * @param runId   Performance test run identifier.
 */
case class PerfEnvStart(perfEnv: PerfEnv, runId: String) extends Command {
  def run(): Unit = {
    perfEnv.machines.foreach { machine =>
      SSH(machine.hostname) { client =>
        machine.services.foreach { service =>
          client.exec(s"bash ${service.startScript}")
        }
      }
    }

    // Upload all the application jars
    perfEnv.applications.foreach { jar =>
      val jarName = jar.split("/").takeRight(1).head
      retry() {
        Http(s"${perfEnv.flink}/jars/upload").postMulti(MultiPart.apply(
          jarName,
          jarName,
          "application/x-java-archive",
          Files.readAllBytes(Paths.get(jar))
        )).asString
      }
    }
  }

  final def retry[T](n: Int = 3, sleep: Int = 1000)(f: => HttpResponse[String]): HttpResponse[String] = {
    val result = Try(f)
    result match {
      case Success(response) if response.isError && n > 1 =>
        Thread.sleep(sleep)
        retry(n - 1, sleep)(f)
      case Success(response) => response
      case Failure(_) if n > 1 =>
        Thread.sleep(sleep)
        retry(n - 1, sleep)(f)
      case Failure(e) => throw e
    }
  }
}

