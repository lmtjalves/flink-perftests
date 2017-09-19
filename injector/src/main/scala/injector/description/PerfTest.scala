package injector.description

import com.typesafe.config.{Config, ConfigException}
import injector.config.InjectorConfig._

import scala.collection.JavaConverters._
import scala.util.Try

/**
 * Contains the description of a performance test with multiple applications.
 */
case class PerfTest(applications: Seq[AppRun]) {

}

object PerfTest {
  /**
   * Reads the [[PerfTest]] from a [[Config]].
   *
   * @param config Configuration that will be used to parse the [[PerfTest]].
   * @return       The parsed [[PerfTest]]
   *
   * @see [[injector.config.InjectorConfig]]
   */
  implicit def fromConfig(config: Config): Try[PerfTest] = Try {
    PerfTest(config.getConfigList("applications").asScala.map(_.read[AppRun].get))
  }
}
