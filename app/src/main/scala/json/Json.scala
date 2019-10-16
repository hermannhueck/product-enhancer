package json

import scala.util.chaining._

sealed trait Json extends Product with Serializable {

  import Json._

  def jsonString: String = this match {
    case JsonNull           => "null"
    case JsonBoolean(bool)  => bool.toString
    case JsonString(string) => string
    case JsonNumber(number) => number.toString
    case JsonArray(items) =>
      items
        .map(_.jsonString)
        .mkString("[", ", ", "]")
    case JsonObject(fields) =>
      fields
        .map { case name -> value => s"{ $name : ${value.jsonString} }" }
        .mkString("{", ", ", "}")
  }

  def toPrettyString: String = this match {
    case JsonObject(fields) =>
      fields
        .map { case name -> value => name -> value.jsonString }
        .map(pair => pair.toString)
        .map(str => s"    $str")
        .mkString("JsonObject(\n  Seq(\n", ",\n", "\n  )\n)")
    case other => other.toString
  }
}

object Json {

  private case object JsonNull                                     extends Json
  private final case class JsonBoolean(value: Boolean)             extends Json
  private final case class JsonString(value: String)               extends Json
  private final case class JsonNumber(value: Double)               extends Json
  private final case class JsonArray(items: Seq[Json])             extends Json
  private final case class JsonObject(fields: Seq[(String, Json)]) extends Json

  private def pairs(p: Product): Seq[(String, Any)] = {
    val names  = p.productElementNames.toList
    val values = (0 until p.productArity).toList map p.productElement
    names zip values
  }

  private def valueToJson(value: Any): Json = value match {
    case b: Boolean        => JsonBoolean(b)
    case s: String         => JsonString(s)
    case d: Double         => JsonNumber(d)
    case f: Float          => JsonNumber(f)
    case i: Int            => JsonNumber(i)
    case l: Long           => JsonNumber(l)
    case None              => JsonNull
    case Some(any)         => valueToJson(any)
    case seq: Seq[Any]     => seq map valueToJson pipe JsonArray
    case array: Array[Any] => array.toSeq pipe valueToJson
    case prod: Product     => prod pipe pairs pipe pairsToJson
    case other             => throw new Exception(s"Cannot encode to Json ... unexpected value: $other")
  }

  private def pairToJson(pair: (String, Any)): (String, Json) = pair match {
    case (name, value) => name -> valueToJson(value)
  }

  def pairsToJson(pairs: Seq[(String, Any)]): Json =
    pairs map pairToJson pipe JsonObject
}
