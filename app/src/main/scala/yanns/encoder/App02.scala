package yanns.encoder

/*
    Source code from Yann Simaons blog post at:
    https://yanns.github.io/blog/2019/10/18/heterogeneous-types-scala/
 */

import io.circe.{Encoder, Json}

import scala.util.chaining._

import util._

object App02 extends App {

  prtTitleObjectName(this)

  prtSubTitle("2. 2nd version uses path dependent types + helper functions for each type to encode")

  trait ToEncode {
    type Value
    def value: Value
    def encoder: Encoder[Value]
  }

  def encode(l: List[ToEncode]): List[Json] =
    l.map(v => v.encoder.apply(v.value))

  def stringToEncode(v: String)(implicit instance: Encoder[String]): ToEncode =
    new ToEncode {
      type Value = String
      def value   = v
      def encoder = instance
    }

  def intToEncode(v: Int)(implicit instance: Encoder[Int]): ToEncode =
    new ToEncode {
      type Value = Int
      def value   = v
      def encoder = instance
    }

  encode(List(stringToEncode("hello"), stringToEncode("world"))) tap println
  // List[io.circe.Json] = List("hello", "world")

  encode(List(intToEncode(1), intToEncode(2), intToEncode(3))) tap println
  // List[io.circe.Json] = List(1, 2, 3)

  "--- encodes also List[Any] ---" tap println
  encode(List(intToEncode(1), stringToEncode("hello"), intToEncode(3))) tap println
  // List[io.circe.Json] = List(1, "hello", 3)

  prtLine()
}
