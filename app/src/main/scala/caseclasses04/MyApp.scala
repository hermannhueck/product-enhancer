/*
  The 4th version of moves the enhancement methods into an implicit class.
  Now these methods can be used with any 'scala.Product', i.e. any case class and any Tuple,
  provided the implicit class is in scope.

  The solution is generic, but the implicit class 'ProductOps' is located inside the object MyApp.
 */

package caseclasses04

import scala.util.chaining._
import util._

import json.Json

case class Employee(name: String, age: Int, manager: Boolean)

case class IceCream(name: String, numCherries: Int, inCone: Boolean)

case class SIB(string: String, int: Int, bool: Boolean)

object MyApp extends App {

  implicit class ProductOps(product: Product) {

    import product._

    val names  = productElementNames.toList
    val values = (0 until productArity).toList map productElement
    val pairs  = names zip values

    @inline def toJson: Json = Json.pairsToJson(pairs)

    @inline def toJsonString: String = toJson.jsonString
  }

  prtTitleObjectName(this)

  "------- Employee as Json ------------------" tap println
  val employee = Employee("John Doe", 42, false)
  employee.toJsonString tap println
  employee.toJson tap println
  employee.toJson.toPrettyString tap println

  "------- IceCream as Json ------------------" tap println
  val iceCream = IceCream("Vanilla", 3, true)
  iceCream.toJsonString tap println
  iceCream.toJson tap println
  iceCream.toJson.toPrettyString tap println

  "------- SIB as Json ----------------------" tap println
  val sib = SIB("foo", 42, true)
  sib.toJsonString tap println
  sib.toJson tap println
  sib.toJson.toPrettyString tap println

  prtLine()
}
