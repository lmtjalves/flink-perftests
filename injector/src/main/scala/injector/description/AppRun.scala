package injector.description

import com.typesafe.config.Config
import injector.config.InjectorConfig._
import injector.util.DurationEnricher._

import scala.concurrent.duration.Duration
import scala.collection.JavaConverters._
import scala.util.Try

/**
 * Keeps a description of a Flink application performance test run.
 *
 * @param name      The name of the application.
 * @param jar       The jar file of the application to upload to Flink.
 * @param startAt   Interval since the beginning of the test, after which the application starts.
 * @param injectors Injectors that will inject data into the Kafka topics that the application reads from.
 */
case class AppRun(
  name: String,
  jar: String,
  startAt: Duration,
  injectors: List[TopicInjector]
)

object AppRun {
  /**
   * Reads the [[AppRun]] from a [[Config]].
   *
   * @param config Configuration that will be used to parse the [[AppRun]].
   * @return       The parsed [[AppRun]]
   *
   * @see [[injector.config.InjectorConfig]]
   */
  implicit def fromConfig(config: Config): Try[AppRun] = Try {
    AppRun(
      config.getString("name"),
      config.getString("jar"),
      config.getDuration("start-at").asScala,
      config.getConfigList("injectors").asScala.map(_.read[TopicInjector].get).toList
    )
  }
}
