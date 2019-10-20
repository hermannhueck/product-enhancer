package product.playground

import scala.util.chaining._

import shapeless._

import util._

object SquaresApp extends App {

  prtTitleObjectName(this)

  object squareMapper extends Poly1 {
    implicit def caseInt    = at[Int](x => x * x)
    implicit def caseLong   = at[Long](x => x * x)
    implicit def caseFloat  = at[Float](x => x * x)
    implicit def caseDouble = at[Double](x => x * x)
    implicit def symbolCase = at[Symbol](_.name)
    implicit def default[A] = at[A](x => x)
  }

  def square[K <: Symbol, HL <: HList](hlist: HL)(
      implicit
      // witness: Witness.Aux[K],
      mapper: ops.hlist.Mapper.Aux[squareMapper.type, HL, HL]
  ): HL =
    hlist map squareMapper

  val numbers = 3 :: 3L :: 3.0f :: 3.0 :: Symbol("sym") :: HNil tap println

  implicit def witness[K <: Symbol](implicit ev: Witness.Aux[K]): Witness.Aux[K] = ev
  // implicit def singletonValue[K <: Symbol](implicit ev: Witness.Aux[K]): ev.T    = ev.value

  // Doesn't work with Symbol out of the box
  // val squares = square(numbers) tap println

  prtLine()
}
