package reporter.config

import com.typesafe.config.Config
import reporter.description.{Machine, PerfEnv, Service}

import scala.util.Try
import scala.collection.JavaConverters._

object ReporterConfig {
  implicit class RichConfig(config: Config) {
    def read[T](implicit parser: Config => Try[T]): Try[T] = {
      parser(config)
    }
  }

  /**
   * Reads the [[PerfEnv]] from a [[Config]].
   *
   * @param config Configuration that will be used to parse the [[PerfEnv]].
   * @return       The parsed [[PerfEnv]].
   */
  implicit def readPerfEnv(config: Config): Try[PerfEnv] = Try {
    PerfEnv(
      config.getConfigList("machines").asScala.map(_.read[Machine].get),
      config.getStringList("applications").asScala,
      config.getString("flink")
    )
  }

  /**
   * Reads the [[Machine]] from a [[Config]].
   *
   * @param config Configuration that will be used to parse the [[Machine]].
   * @return       The parsed [[Machine]].
   */
  implicit def readMachine(config: Config): Try[Machine] = Try {
    Machine(
      config.getString("hostname"),
      config.getConfigList("services").asScala.map(_.read[Service].get)
    )
  }

  /**
   * Reads the [[Service]] from a [[Config]].
   *
   * @param config Configuration that will be used to parse the [[Service]].
   * @return       The parsed [[Service]].
   */
  implicit def readService(config: Config): Try[Service] = Try {
    Service(
      config.getString("start"),
      config.getString("stop"),
      Try { config.getString("logs") } toOption
    )
  }
}
