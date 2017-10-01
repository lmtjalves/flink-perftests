package injector

import com.typesafe.config.ConfigFactory
import injector.config.InjectorConfig._
import injector.model.PerfTest
import io.gatling.core.Predef.Simulation

import scala.util.{Failure, Success}

class Injector extends Simulation {
  // Read the test specification and execute it
  val injectorConfig = ConfigFactory.load("injector.conf")

  injectorConfig.read[PerfTest] match {
    case Success(perfTests) =>
      setUp(perfTests.asGatling():_*)

    case Failure(e) => throw e
  }
}
