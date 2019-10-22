package yanns.encoder

/*
    Source code from Yann Simaons blog post at:
    https://yanns.github.io/blog/2019/10/18/heterogeneous-types-scala/
 */

import io.circe.{Encoder, Json}

import scala.util.chaining._
import scala.language.implicitConversions

import util._

object App05 extends App {

  prtTitleObjectName(this)

  prtSubTitle("5. implemented cache for ToEncode instances")

  trait ToEncode {
    type Value
    def value: Value
    def encoder: Encoder[Value]
  }

  object ToEncode {

    import scala.reflect.runtime.universe._

    var cache = Map[String, ToEncode]()

    implicit def fromEncoder[A: Encoder: TypeTag](a: A): ToEncode = {
      val key = typeOf[A].toString
      cache.get(key) match {
        case Some(toEncode) =>
          toEncode
        case None =>
          val toEncode = createInstance(key, a)
          cache += key -> toEncode
          toEncode
      }
    }

    def createInstance[A: Encoder](key: String, a: A) =
      new ToEncode {
        s"--> generating new ToEncode instance for ${key} ..." tap println
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

  encode(List(1, "hello", 3.0, false, Some(42), List(42))) tap println

  prtLine()
}
