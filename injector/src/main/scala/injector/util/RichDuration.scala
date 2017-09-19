package injector.util

import java.time.{Duration => JDuration}

import scala.concurrent.duration.Duration

object DurationEnricher {
  implicit class RichJavaDuration(duration: JDuration) {
    /**
     * Converts a [[JDuration]] into a [[Duration]].
     */
    def asScala: Duration = Duration.fromNanos(duration.toNanos)
  }
}
