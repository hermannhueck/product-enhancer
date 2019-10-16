/*
  The 1st version enhances the case class SIB inside its class body.
  It provides:
    - 'names': a Tuple with the names of the case class members.
    - 'values': a Tuple with the values of the case class members.
    - 'pairs': a Tuple with the name-value-pairs of the case class members.
    - 'toJsonString': which encodes a case class instances into a Json String
  The drawbacks of this solution:
    - It should encode the case class instance into a Json structure.
    - It is not generic. The implementation must be repeated for every case class.
 */

package caseclasses01

import scala.util.chaining._
import util._

case class Employee(name: String, age: Int, manager: Boolean)

case class IceCream(name: String, numCherries: Int, inCone: Boolean)

case class SIB(string: String, int: Int, bool: Boolean) {

  val names  = ("string", "int", "bool")
  val values = (string, int, bool)
  val pairs  = (names._1 -> values._1, names._2 -> values._2, names._3 -> values._3)

  private def pairToJsonString(pair: (String, Any)): String = pair match {
    case (name, value) => s"{ $name : ${value.toString} }"
  }

  def toJsonString(): String =
    s"{ ${pairToJsonString(pairs._1)}, ${pairToJsonString(pairs._2)}, ${pairToJsonString(pairs._3)} }"
}

object MyApp extends App {

  prtTitleObjectName(this)

  val sib = SIB("foo", 42, true)

  "------- Json string ------------------" tap println
  sib.toJsonString() tap println

  prtLine()
}
