val projectName        = "product-enhancer"
val projectDescription = "Enhance Products (Scala Case Classes an Tuples) in a generic way"
val projectVersion     = "0.1.0-SNAPSHOT"

val scala212               = "2.12.10"
val scala213               = "2.13.1"
val supportedScalaVersions = List(scala213)

def scalaReflect(scalaVersion: String) = "org.scala-lang"         % "scala-reflect"            % scalaVersion
val collectionCompat                   = "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.2"
val shapeless                          = "com.chuusai"            %% "shapeless"               % "2.3.3"
val circe                              = "io.circe"               %% "circe-core"              % "0.12.3"

val scalaTest  = "org.scalatest"  %% "scalatest"  % "3.0.8"
val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.14.2"

inThisBuild(
  Seq(
    version := projectVersion,
    scalaVersion := scala213,
    crossScalaVersions := supportedScalaVersions,
    publish / skip := true,
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8",            // source files are in UTF-8
      "-deprecation",     // warn about use of deprecated APIs
      "-unchecked",       // warn about unchecked type parameters
      "-feature",         // warn about misused language features
      "-Xlint",           // enable handy linter warnings
      "-explaintypes",    // explain type errors in more detail
      "-Xfatal-warnings", // fail the compilation if there are any warnings
      "-Xcheckinit"       // wrap field accessors to throw an exception on uninitialized access
    ),
    libraryDependencies ++= Seq(
      scalaReflect(scalaVersion.value),
      collectionCompat,
      shapeless,
      circe,
      scalaTest  % Test,
      scalaCheck % Test
    ),
    initialCommands :=
      s"""|
          |import scala.util.chaining._
          |import shapeless._
          |import shapeless.record._
          |import shapeless.ops.record._
          |import shapeless.ops.hlist
          |import shapeless.tag._
          |import io.circe._
          |println
          |""".stripMargin // initialize REPL
  )
)

lazy val root = (project in file("."))
  .aggregate(app)
  .settings(
    name := projectName,
    description := projectDescription,
    crossScalaVersions := Seq.empty,
    scalacOptions ++= {
      scalaVersion.value match {
        case version if version.startsWith("2.13") => Seq.empty
        case version =>
          Seq(
            "-Ypartial-unification", // (removed in scala 2.13) allow the compiler to unify type constructors of different arities
            "-language:higherKinds"  // (not required since scala 2.13.1) suppress warnings when using higher kinded types
          )
      }
    }
  )

lazy val app = (project in file("app"))
  .dependsOn(util)
  .settings(
    name := "app",
    description := projectDescription,
    libraryDependencies ++= Seq(
      shapeless
    )
  )

lazy val util = (project in file("util"))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    name := "util",
    description := "Utilities",
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "build"
  )

// https://github.com/typelevel/kind-projector
addCompilerPlugin("org.typelevel" % "kind-projector" % "0.11.0" cross CrossVersion.full)
// https://github.com/oleg-py/better-monadic-for
addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
