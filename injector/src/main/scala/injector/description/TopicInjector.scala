package injector.description


import com.typesafe.config.Config
import injector.util.DurationEnricher._

import scala.concurrent.duration.Duration
import scala.util.Try
import scala.collection.JavaConverters._

/**
 * Keeps a description of an Apache Kafka data injector for a specific topic.
 *
 * @param name      The topic name.
 * @param source    The input file that will be used to generate data for the topic.
 * @param intervals A sequence that contains a description of how the injection rate should change over time.
 *                  This information is passed in form of (duration, input rate) meaning that it will send
 *                  for the specified duration data at the specified input rate. After it finishes, it moves to the
 *                  next interval.
 */
case class TopicInjector(
  name: String,
  source: String,
  kafka: String,
  intervals: Seq[(Int, Duration)]
)

object TopicInjector {
  /**
   * Reads the [[TopicInjector]] from a [[Config]].
   *
   * @param config Configuration that will be used to parse the [[TopicInjector]].
   * @return       The parsed [[TopicInjector]]
   *
   * @see [[injector.config.InjectorConfig]]
   */
  implicit def fromConfig(config: Config): Try[TopicInjector] = Try {
    TopicInjector(
      config.getString("name"),
      config.getString("source"),
      config.getString("kafka"),
      config.getConfigList("intervals").asScala.map { intervalConfig =>
        intervalConfig.getInt("rate") -> intervalConfig.getDuration("duration").asScala
      }
    )
  }
}