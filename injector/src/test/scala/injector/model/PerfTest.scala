package injector.model

import io.gatling.core.structure.PopulationBuilder

/**
 * Contains the description of a performance test with multiple applications.
 */
case class PerfTest(applications: Seq[AppPerfTest]) {
  def asGatling(): Seq[PopulationBuilder] = applications.map(_.asGatling()).flatten
}
