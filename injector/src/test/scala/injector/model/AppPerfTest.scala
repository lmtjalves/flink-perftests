package injector.model

import injector.gatling.FlinkApp
import io.gatling.core.structure.PopulationBuilder
import io.gatling.http.Predef.http

import scala.concurrent.duration.FiniteDuration

/**
 * Keeps a description of a Flink application performance test run.
 *
 * @param jar         Jar file that was uploaded to Flink.
 * @param startAt     Interval since the beginning of the test, after which the application starts.
 * @param flinkMaster Apache Flink master hostname.
 * @param injectors   Injectors that will inject data into the Kafka topics that the application reads from.
 */
case class AppPerfTest(
  jar:         String,
  startAt:     FiniteDuration,
  flinkMaster: String,
  injectors:   List[TopicInjector]
) {
  def asGatling(): Seq[PopulationBuilder] = {
    import io.gatling.core.Predef._

    // Duration of the application test
    val testDuration = injectors.map(_.duration).max

    val flinkApp = FlinkApp(flinkMaster, jar)

    // Create scenario to start/stop the application
    val appSubmissionScenario = scenario(jar)
      // Wait till it's time for the application to start
      .pause(startAt)
      // Start the application
      .exec(flinkApp.start())
      // Wait for all application injectors to finish
      .pause(testDuration)
      // Stop the application
      .exec(flinkApp.stop())
      // We only want to do this sequence once per application performance test
      .inject(atOnceUsers(1))
      .protocols(http.baseURL(flinkMaster))

    // Create scenarios for the injectors
    val injectorScenarios = injectors.map(_.asGatling())

    injectorScenarios :+ appSubmissionScenario
  }
}
