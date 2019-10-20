package product.enhancer07

import scala.util.chaining._

import product.json.Json

import shapeless._
import shapeless.record._
import shapeless.ops.record.Keys
import shapeless.ops.hlist
import shapeless.ops.hlist.Selector
import shapeless.tag._
import shapeless.syntax.typeable._

object ProductEnhancer {

  implicit class Ops[P <: Product, HL <: HList](product: P)(
      implicit
      val gen: LabelledGeneric.Aux[P, HL]
  ) {

    import product._

    val names  = productElementNames.toList
    val values = (0 until productArity).toList map productElement
    val pairs  = names zip values

    @inline def toJson: Json = Json.pairsToJson(pairs)

    @inline def toJsonString: String = toJson.jsonString

    //import syntax.std.tuple._
    //import syntax.std.values._

    val repr: gen.Repr = gen.to(product)
    // Typeable.apply[gen.Repr].describe tap println
    // val hlistValues = repr.values
    // val hlistKeys = repr.keys
    // val hlistKeys   = Keys[gen.Repr].apply()
  }
}
