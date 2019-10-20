package product.enhancer05

import product.json.Json

object ProductEnhancer {

  implicit class Ops(product: Product) {

    import product._

    val names  = productElementNames.toList
    val values = (0 until productArity).toList map productElement
    val pairs  = names zip values

    @inline def toJson: Json = Json.pairsToJson(pairs)

    @inline def toJsonString: String = toJson.jsonString
  }
}
