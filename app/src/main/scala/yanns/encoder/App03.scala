package yanns.encoder

/*
    Source code from Yann Simaons blog post at:
    https://yanns.github.io/blog/2019/10/18/heterogeneous-types-scala/
 */

import io.circe.{Encoder, Json}

import scala.util.chaining._
import scala.language.implicitConversions

import util._

object App03 extends App {

  prtTitleObjectName(this)

  prtSubTitle("3. Make helper functions implicit which simplifies code at the call site")

  trait ToEncode {
    type Value
    def value: Value
    def encoder: Encoder[Value]
  }

  def encode(l: List[ToEncode]): List[Json] =
    l.map(v => v.encoder.apply(v.value))

  implicit def stringToEncode(v: String)(implicit instance: Encoder[String]): ToEncode =
    new ToEncode {
      type Value = String
      def value   = v
      def encoder = instance
    }

  implicit def intToEncode(v: Int)(implicit instance: Encoder[Int]): ToEncode =
    new ToEncode {
      type Value = Int
      def value   = v
      def encoder = instance
    }

  encode(List("hello", "world")) tap println
  // List[io.circe.Json] = List("hello", "world")

  encode(List(1, 2, 3)) tap println
  // List[io.circe.Json] = List(1, 2, 3)

  "--- encodes also List[Any] ---" tap println
  encode(List(1, "hello", 3)) tap println
  // List[io.circe.Json] = List(1, "hello", 3)

  prtLine()
}
