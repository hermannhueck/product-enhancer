package product.playground

import shapeless._
import shapeless.ops.record.Keys
import shapeless.ops.hlist
import shapeless.ops.hlist.Selector
import shapeless.tag._

import scala.util.chaining._

import util._
import shapeless.ops.hlist.Mapper

object ShapelessPlayground extends App {

  prtTitleObjectName(this)

  def getFieldName[A, F]: GetFieldName[A, F] = new GetFieldName

  class GetFieldName[A, F] {

    def get[L <: HList, KeyList <: HList](
        implicit
        // we need to be able to convert our case class `A` to a labelled hlist `L`
        ev: LabelledGeneric.Aux[A, L],
        // we grab the keys of the labelled `L`, into an hlist `KeyList`
        ev2: Keys.Aux[L, KeyList],
        // from the key list, we select the first field with a name matching `F`
        ev3: Selector[KeyList, Symbol with Tagged[F]]
    ): String = {
      val keys: KeyList = Keys[ev.Repr].apply()

      keys.select[Symbol with Tagged[F]].name
    }
  }

  def getFieldSymbols[CC]: GetFieldSymbols[CC] = new GetFieldSymbols

  class GetFieldSymbols[CC] {

    def get[L <: HList, KeyList <: HList](
        implicit
        // we need to be able to convert our case class `A` to a labelled hlist `L`
        ev: LabelledGeneric.Aux[CC, L],
        // we grab the keys of the labelled `L`, into an hlist `KeyList`
        ev2: Keys.Aux[L, KeyList]
        // mapper: hlist.Mapper.Aux[symbol2NameMapper.type, KeyList, KeyList]
    ): KeyList = {
      val keys: KeyList = Keys[ev.Repr].apply()

      keys // .map(symbol2NameMapper)
    }
  }

  trait HListtMapper[KeyList <: HList, P] {
    def apply(a: KeyList): KeyList
  }

  implicit def genericHListMapper[KeyList <: HList](
      implicit
      mapper: hlist.Mapper.Aux[symbol2NameMapper.type, KeyList, KeyList]
  ): HListtMapper[KeyList, symbol2NameMapper.type] =
    new HListtMapper[KeyList, symbol2NameMapper.type] {

      def apply(a: KeyList): KeyList =
        mapper.apply(a)
    }

  case class SIB(string: String, int: Int, bool: Boolean)

  val sib = SIB("foo", 42, false)

  "name of a case class field" tap println
  getFieldName[SIB, Witness.`"string"`.T].get tap println

  "all case class field name es symbols" tap println
  val symbols = getFieldSymbols[SIB].get tap println

  object symbol2NameMapper extends Poly1 {
    implicit val symbolCase = at[Symbol](_.name)
    implicit def default[A] = at[A](a => throw new Exception)
  }

  /*
    implicit def mapper[In <: HList, O <: HList]
        : Mapper[symbol2NameMapper.type, In] { type Out = O } =
      new Mapper[symbol2NameMapper.type, In] {

        type Out = O

        def apply(in: In): Out = ???
      }
   */

  def symbols2Names[HL <: HList](symbols: HL)(
      implicit
      mapper: Mapper[symbol2NameMapper.type, HL] { type Out = HL }
  ) =
    symbols map symbol2NameMapper

  // symbols2Names(symbols) tap println

  import record._

  println
  val gen  = LabelledGeneric[SIB]
  val repr = gen.to(sib)
  "values: " tap println
  val values = repr.values tap println
  "keys: " tap println
  val keys = repr.keys tap println

  prtLine()
}
