package example

import org.scalatest._
import reporter.Hello

class HelloSpec extends FlatSpec with Matchers {
  "The Hello object" should "say hello" in {
    Hello.greeting shouldEqual "hello"
  }
}
