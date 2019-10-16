# Enhance Products (Scala Case Classes an Tuples) in a generic way

This small project enhances 'scala.Product's in a generic way.
It the following methods for any cases class or Tuple:

- 'names': provides the names of all Product members as a List[String]
- 'values': provides the values of all Product members as a List[Any]
- 'pairs': provides the name-value-pairss of all Product members as a List[(String, Any)]
- 'toJson': encodes the Product into a JSON structure
- 'toJsonString': encodes the Product into a JSON string.

These enhancement methods are provided by an implicit class for every Product
which has the implicit class in scope.

Due to the extended methods on 'scala.Product' this solution is only possible in Scaala 2.13.x and above.

Several subsequent steps lead to the final solution.
