/*
  Tuples are very specific in their types which prevents us from making the solution generic.
  The 3rd version of the programm uses provides 'names', 'values' and 'pairs' as Lists.
  The implementation use generic methods of 'scala.Product'
  which releases the dependency on a specific case class.

  The solution is still not generic, as the implemention is still in the body of a specific case class.
  But the impl does no longer depend on this case class but only on 'scala.Product'.
 */

package product.enhancer03

import scala.util.chaining._
import util._

import product.json.Json

case class Employee(name: String, age: Int, manager: Boolean)

case class IceCream(name: String, numCherries: Int, inCone: Boolean)

case class SIB(string: String, int: Int, bool: Boolean) {

  val names  = productElementNames.toList
  val values = (0 until productArity).toList map productElement
  val pairs  = names zip values

  def toJson: Json = Json.pairsToJson(pairs)

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
