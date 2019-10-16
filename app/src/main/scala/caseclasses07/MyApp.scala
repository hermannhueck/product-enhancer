/*
  In the 7th version I tried to bring the Tuples back with the help of shapeless.
  But this approach was not successful.
 */

package caseclasses07

import scala.util.chaining._
import util._

case class Employee(name: String, age: Int, manager: Boolean)

case class IceCream(name: String, numCherries: Int, inCone: Boolean)

case class SIB(string: String, int: Int, bool: Boolean)

case class SIBOSCC(
    string: String,
    int: Int,
    bool: Boolean,
    opt: Option[String],
    seq: Seq[Int],
    sib: SIB // nested case class
)

case class SIBOSP(
    string: String,
    int: Int,
    bool: Boolean,
    opt: Option[String],
    seq: Seq[Int],
    prod: Product // nested Product allows passing any case class or any tuple
)

object MyApp extends App {

  prtTitleObjectName(this)

  import ProductEnhancer._

  import shapeless._
  import shapeless.record._
  import shapeless.ops.record._

  implicit val genSib     = LabelledGeneric[SIB]
  implicit val genSiboscc = LabelledGeneric[SIBOSCC]
  implicit val genSibosp  = LabelledGeneric[SIBOSP]

  val sib = SIB("foo", 42, false)

  genSib.to(sib).values tap println
  genSib.to(sib).keys tap println

  println
  sib.repr tap println
  // sib.repr.values tap println

  prtLine()
}
