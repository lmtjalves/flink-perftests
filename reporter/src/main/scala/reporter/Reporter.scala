package reporter

import com.typesafe.config.ConfigFactory

object Reporter extends App {
  val injectorConfig = ConfigFactory.load("injector.conf")
  println(injectorConfig)
}
