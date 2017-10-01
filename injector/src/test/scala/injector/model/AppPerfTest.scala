package injector.model

import injector.gatling.FlinkApp
import io.gatling.core.structure.PopulationBuilder
import io.gatling.http.Predef.http
import org.json4s._
import org.json4s.native.JsonMethods._

import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.util.Try
import scalaj.http.Http

/**
 * Keeps a description of a Flink application performance test run.
 *
 * @param jar         Jar filename (not path) that was uploaded to Flink.
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
  lazy val jarId: String = {
    implicit val formats = DefaultFormats

    val response = parse(Http(s"${flinkMaster}/jars").asString.body)

    (response \ "files").extract[List[Map[String, Any]]]
      .map { jarFile => (jarFile("id").toString, jarFile("name").toString, jarFile("uploaded").toString.toLong) }
      .filter(_._2.equals(jar))
      .maxBy(_._3)._1
  }

  def asGatling(): Seq[PopulationBuilder] = {
    import io.gatling.core.Predef._

    // Duration of the application test
    val testDuration = Try(injectors.map(_.duration).max).getOrElse(Duration("0s"))

    val flinkApp = FlinkApp(flinkMaster, jarId)

    // Create scenario to start/stop the application
    val appSubmissionScenario = scenario(jarId)
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
