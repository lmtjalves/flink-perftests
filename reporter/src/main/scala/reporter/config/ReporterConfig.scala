package reporter.config

import com.typesafe.config.Config
import reporter.description.Machine

import scala.util.Try

object ReporterConfig {
  implicit class RichConfig(config: Config) {
    def read[T](implicit parser: Config => Try[T]) = {
      parser(config)
    }
  }

  /**
   * Reads the [[Machine]] from a [[Config]].
   *
   * @param config Configuration that will be used to parse the [[Machine]].
   * @return       The parsed [[Machine]].
   *
   * @see [[ReporterConfig]]
   */
  implicit def readMachine(config: Config): Try[Machine] = Try {
    Machine(
      config.getString("hostname"),
      config.getString("start"),
      config.getString("stop"),
      Try(config.getString("logs")).toOption
    )
  }
}
