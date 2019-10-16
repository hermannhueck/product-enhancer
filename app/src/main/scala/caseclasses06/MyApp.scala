/*
  The 6th version of the program does not contain any design improvements.
  It just demonstrates the solution with other case classes and Tuples, also nested ones.
 */

package caseclasses06

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

  val sib      = SIB("foo", 42, false)
  val siboscc1 = SIBOSCC("foo", 42, false, None, Seq.empty, sib)
  showProduct(siboscc1) tap println

  "\u2500" * 50 tap println
  val siboscc2 = SIBOSCC("bar", 43, true, Some("bar"), Seq(44, 45, 46), sib)
  showProduct(siboscc2) tap println

  "\u2500" * 50 tap println
  val siboscc2Tupled = SIBOSCC.unapply(siboscc2).get
  showProduct(siboscc2Tupled) tap println

  "\u2500" * 50 tap println
  val sibTupled    = SIB.unapply(sib).get
  val sibosp       = SIBOSP("bar", 43, true, Some("bar"), Seq(44, 45, 46), sibTupled)
  val sibospTupled = SIBOSP.unapply(sibosp).get
  showProduct(sibospTupled) tap println

  prtLine()

  def showProduct(p: Product): String =
    s"""|${p.toJsonString}
        |${p.toJson}
        |${p.toJson.toPrettyString}
        |
        |${p.names}
        |${p.values}
        |${p.pairs}""".stripMargin
}
