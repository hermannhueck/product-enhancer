package yanns.encoder

/*
    Source code from Yann Simaons blog post at:
    https://yanns.github.io/blog/2019/10/18/heterogeneous-types-scala/
 */

import io.circe.{Encoder, Json}

import scala.util.chaining._

import util._

object App01 extends App {

  prtTitleObjectName(this)

  prtSubTitle("1. 1st version cannot encode heterogenious List[Any]")

  def encode[A: Encoder](l: List[A]): List[Json] =
    l.map(Encoder[A].apply)

  encode(List("hello", "world")) tap println
  // List[io.circe.Json] = List("hello", "world")

  encode(List(1, 2, 3)) tap println
  // List[io.circe.Json] = List(1, 2, 3)

  "--- does not encode List[Any] ---" tap println
  // encode(List(1, "hello", 3))
  // error: diverging implicit expansion for type io.circe.Encoder[Any]

  prtLine()
}
