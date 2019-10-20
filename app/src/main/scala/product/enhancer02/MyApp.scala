/*
  The 2nd version improves the 1st one by encoding a case class instance
  into a Json structure (see json.Json.scala) which then can be stringified.

  The solution still uses Tuples and is not generic, not usable for any case class or Tuple.
 */

package product.enhancer02

import scala.util.chaining._
import util._

import product.json.Json

case class Employee(name: String, age: Int, manager: Boolean)

case class IceCream(name: String, numCherries: Int, inCone: Boolean)

case class SIB(string: String, int: Int, bool: Boolean) {

  val names  = ("string", "int", "bool")
  val values = (string, int, bool)
  val pairs  = (names._1 -> values._1, names._2 -> values._2, names._3 -> values._3)

  def toJson: Json = Json.pairsToJson(Seq(pairs._1, pairs._2, pairs._3))

  def toJsonString: String = toJson.jsonString
}

object MyApp extends App {

  prtTitleObjectName(this)

  val sib = SIB("foo", 42, true)

  "------- Json string ------------------" tap println
  sib.toJsonString tap println

  println
  "------- Json data structure ----------" tap println
  sib.toJson tap println
  println
  sib.toJson.toPrettyString tap println

  prtLine()
}
