package yanns.encoder

/*
    Source code from Yann Simaons blog post at:
    https://yanns.github.io/blog/2019/10/18/heterogeneous-types-scala/
 */

import io.circe.{Encoder, Json}

import scala.util.chaining._
import scala.language.implicitConversions

import util._

object App06 extends App {

  prtTitleObjectName(this)

  prtSubTitle("6. Yann's solution inspired by Travis brown")

  type AsJson = Json

  object AsJson {
    // implicit conversion turns every a that has an Encoder into an AsJson
    implicit def toAsJson[A: Encoder](a: A): AsJson = Encoder[A].apply(a)
  }
  import AsJson._

  def encode(l: List[AsJson]): List[Json] =
    l

  val encodedStrings = encode(List("hello", "world"))
  encodedStrings == List(Json.fromString("hello"), Json.fromString("world")) pipe println

  encode(List("hello", "world")) pipe println
  encode(List(1, 2, 3)) pipe println
  encode(List(1, "hello", 3)) pipe println

  encode(List(1, "hello", 3.0, false, Some(42), List(42))) pipe println

  prtLine()
}
