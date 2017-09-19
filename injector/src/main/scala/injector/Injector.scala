package injector

import com.typesafe.config.ConfigFactory
import injector.config.InjectorConfig._
import injector.description.PerfTest

import scala.util.{Failure, Success}

object Injector extends App {
  // Read the test specification and execute it
  val injectorConfig = ConfigFactory.load("injector.conf")

  injectorConfig.read[PerfTest] match {
    case Success(perfTests) => {
      println(perfTests)
    }
    case Failure(e) => throw e
  }
}
