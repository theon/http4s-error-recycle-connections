name         := "http4s-error-recycle-connections"
scalaVersion := "2.13.5"

val http4sVersion = "0.21.22"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "ch.qos.logback" % "logback-core" % "1.2.3",
  "co.fs2" %% "fs2-core" % "2.5.4",
  "org.http4s" %% "http4s-async-http-client" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-ember-client" % http4sVersion,
  "org.http4s" %% "http4s-client" % http4sVersion,
  "org.http4s" %% "http4s-core" % http4sVersion,
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.slf4j" % "slf4j-api" % "1.7.30",
  "org.typelevel" %% "cats-core" % "2.5.0",
  "org.typelevel" %% "cats-effect" % "2.4.1"
)
