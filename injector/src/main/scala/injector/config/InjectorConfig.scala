package injector.config

import com.typesafe.config.Config

import scala.util.Try

object InjectorConfig {
  implicit class RichConfig(config: Config) {
    def read[T](implicit parser: Config => Try[T]) = {
      parser(config)
    }
  }
}
