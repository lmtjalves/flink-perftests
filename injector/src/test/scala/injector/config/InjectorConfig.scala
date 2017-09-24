package injector.config

import injector.model.{AppPerfTest, PerfTest, TopicInjector}
import com.typesafe.config.Config
import injector.util.DurationEnricher._

import scala.concurrent.duration.FiniteDuration
import scala.collection.JavaConverters._
import scala.util.Try

object InjectorConfig {
  implicit class RichConfig(config: Config) {
    def read[T](implicit parser: Config => Try[T]) = {
      parser(config)
    }

    def getFiniteDuration(path: String): FiniteDuration = {
      val duration = config.getDuration(path).asScala
      duration match {
        case finiteDuration: FiniteDuration => finiteDuration
        case _ => throw new Exception("Duration should be finite")
      }
    }
  }

  /**
   * Reads the [[PerfTest]] from a [[Config]].
   *
   * @param config Configuration that will be used to parse the [[PerfTest]].
   * @return       The parsed [[PerfTest]].
   *
   * @see [[injector.config.InjectorConfig]]
   */
  implicit def readPerfTest(config: Config): Try[PerfTest] = Try {
    PerfTest(config.getConfigList("applications").asScala.map(_.read[AppPerfTest].get))
  }

  /**
   * Reads the [[AppPerfTest]] from a [[Config]].
   *
   * @param config Configuration that will be used to parse the [[AppPerfTest]].
   * @return       The parsed [[AppPerfTest]]
   * @see [[injector.config.InjectorConfig]]
   */
  implicit def readAppPerfTest(config: Config): Try[AppPerfTest] = Try {
    AppPerfTest(
      config.getString("jar"),
      config.getFiniteDuration("start-at"),
      config.getString("flink"),
      config.getConfigList("injectors").asScala.map(_.read[TopicInjector].get).toList
    )
  }

  /**
   * Reads the [[TopicInjector]] from a [[Config]].
   *
   * @param config Configuration that will be used to parse the [[TopicInjector]].
   * @return       The parsed [[TopicInjector]]
   *
   * @see [[injector.config.InjectorConfig]]
   */
  implicit def readTopicInjector(config: Config): Try[TopicInjector] = Try {
    TopicInjector(
      config.getString("topic"),
      config.getString("source"),
      config.getString("kafka"),
      config.getConfigList("intervals").asScala.map { intervalConfig =>
        intervalConfig.getInt("rate") -> intervalConfig.getFiniteDuration("duration")
      }
    )
  }
}
