name:="postgis_slick"

version:="0.1"

scalaVersion:="2.10.0"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"

libraryDependencies += "com.typesafe.slick" %% "slick" % "1.0.0"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.0.0-M8"

libraryDependencies += "postgresql" % "postgresql" % "9.1-901.jdbc4"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

