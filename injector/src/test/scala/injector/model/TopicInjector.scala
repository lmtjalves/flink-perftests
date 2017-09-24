package injector.model

import io.gatling.core.structure.PopulationBuilder
import org.apache.kafka.clients.producer.ProducerConfig

import scala.concurrent.duration.FiniteDuration

/**
 * Keeps a description of an Apache Kafka data injector for a specific topic.
 *
 * @param topic      The topic name.
 * @param source     The input file that will be used to generate data for the topic.
 * @param kafkaHosts A list of host/port pairs to use for establishing the initial connection to the Kafka cluster.
 * @param intervals  A sequence that contains a description of how the injection rate should change over time.
 *                   This information is passed in form of (duration, input rate) meaning that it will send
 *                   for the specified duration data at the specified input rate. After it finishes, it moves to the
 *                   next interval.
 */
case class TopicInjector(
  topic:      String,
  source:     String,
  kafkaHosts: String,
  intervals:  Seq[(Int, FiniteDuration)]
) {

  /**
   * Returns for how long the injector will inject data.
   */
  def duration(): FiniteDuration = {
    intervals.map { case (_, duration) => duration } reduce(_ + _)
  }

  def asGatling(): PopulationBuilder = {
    import io.gatling.core.Predef._
    import com.github.mnogu.gatling.kafka.Predef._

    val kafkaConf = kafka
      .topic(topic)
      .properties(Map(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG      -> kafkaHosts,
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG   -> "org.apache.kafka.common.serialization.StringSerializer",
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringSerializer"
      ))

    scenario(topic)
      // Feed data from the file by picking random records
      .feed(csv(source).random)
      // Send that data to Kafka
      .exec(kafka("").send[String]("${foo}"))
      // Specify how the injection rate (constantUsersPerSec) changes over time
      .inject(intervals.map { case (rate, duration) => constantUsersPerSec(rate) during(duration) })
      .protocols(kafkaConf)
  }
}
