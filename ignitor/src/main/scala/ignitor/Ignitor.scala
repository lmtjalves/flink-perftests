package ignitor

import com.typesafe.config.ConfigFactory

object Ignitor extends App {
  val injectorConfig = ConfigFactory.load("injector.conf")
  println(injectorConfig)
}
