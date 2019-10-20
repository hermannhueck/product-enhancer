/*
  In the 5th version the implicit class has been moved into it' own file 'ProductEnhancer.scala'
 */

package product.enhancer05

import scala.util.chaining._
import util._

case class Employee(name: String, age: Int, manager: Boolean)

case class IceCream(name: String, numCherries: Int, inCone: Boolean)

case class SIB(string: String, int: Int, bool: Boolean)

object MyApp extends App {

  prtTitleObjectName(this)

  import ProductEnhancer._

  "------- Employee as Json ------------------" tap println
  val employee = Employee("John Doe", 42, false)
  showProduct(employee) tap println

  "------- IceCream as Json ------------------" tap println
  val iceCream = IceCream("Vanilla", 3, true)
  showProduct(iceCream) tap println

  "------- SIB as Json ----------------------" tap println
  val sib = SIB("foo", 42, true)
  showProduct(sib) tap println

  println
  sib.names tap println
  sib.values tap println
  sib.pairs tap println

  prtLine()

  def showProduct(p: Product): String =
    s"${p.toJsonString}\n${p.toJson}\n${p.toJson.toPrettyString}"
}
