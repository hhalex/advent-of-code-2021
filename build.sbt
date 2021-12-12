val scala3Version = "3.1.0"
val http4sVersion = "1.0.0-M30"

lazy val root = project
  .in(file("."))
  .settings(
    name := "advent-of-code-2021",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,
    // https://mvnrepository.com/artifact/co.fs2/fs2-core
    libraryDependencies += "co.fs2" %% "fs2-core" % "3.2.3",
    libraryDependencies += "co.fs2" %% "fs2-io" % "3.2.3",
    libraryDependencies +="org.http4s" %% "http4s-dsl" % http4sVersion,
    libraryDependencies +="org.http4s" %% "http4s-blaze-server" % http4sVersion,
    libraryDependencies +="org.http4s" %% "http4s-blaze-client" % http4sVersion,

    // https://mvnrepository.com/artifact/org.typelevel/cats-effect-testing-specs2
    libraryDependencies += "org.typelevel" %% "cats-effect-testing-scalatest" % "1.4.0" % Test

  )
