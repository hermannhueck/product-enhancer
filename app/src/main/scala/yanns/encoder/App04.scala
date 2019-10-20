package yanns.encoder

/*
    Source code from Yann Simaons blog post at:
    https://yanns.github.io/blog/2019/10/18/heterogeneous-types-scala/
 */

import io.circe.{Encoder, Json}

import scala.util.chaining._
import scala.language.implicitConversions

import util._

object App04 extends App {

  prtTitleObjectName(this)

  prtSubTitle("4. replace helper with implicit generator which generates ToEncode instances ad hoc")

  trait ToEncode {
    type Value
    def value: Value
    def encoder: Encoder[Value]
  }

  object ToEncode {

    implicit def fromEncoder[A: Encoder](a: A): ToEncode =
      new ToEncode {
        "--> generating new ToEncode instance ..." tap println
        type Value = A
        def value   = a
        def encoder = Encoder[A]
      }
  }

  def encode(l: List[ToEncode]): List[Json] =
    l.map(v => v.encoder.apply(v.value))

  encode(List("hello", "world")) tap println
  // List[io.circe.Json] = List("hello", "world")

  encode(List(1, 2, 3)) tap println
  // List[io.circe.Json] = List(1, 2, 3)

  "--- encodes also List[Any] ---" tap println
  encode(List(1, "hello", 3)) tap println
  // List[io.circe.Json] = List(1, "hello", 3)

  "--- Drawback: generates a ToEncode instance for each element of the List ---" tap println

  prtLine()
}
