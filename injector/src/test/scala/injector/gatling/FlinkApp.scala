package injector.gatling

import io.gatling.http.request.builder.HttpRequestBuilder

/**
 * Abstract a Flink master host.
 * @see https://ci.apache.org/projects/flink/flink-docs-release-1.3/monitoring/rest_api.html
 */
case class FlinkApp(hostname: String, jar: String) {
  import io.gatling.core.Predef._
  import io.gatling.http.Predef._

  /**
   * Starts an application.
   */
  def start(): HttpRequestBuilder = http(jar)
    .post(s"${hostname}/jars/${jar}/run")
    .check(status.is(200))
    .check(jsonPath("$.jobid").saveAs("jobId"))

  /**
   * Stops and application.
   */
  def stop(): HttpRequestBuilder = http(jar)
    .delete(s"${hostname}/jobs/${jar}/cancel")
}
