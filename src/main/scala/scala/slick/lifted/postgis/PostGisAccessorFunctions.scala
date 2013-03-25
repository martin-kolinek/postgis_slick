package scala.slick.lifted.postgis

import scala.slick.lifted.SimpleFunction
import scala.slick.postgis.Geometry

trait PostGisAccessorFunctions {
	val getXCoord = SimpleFunction.unary[Geometry, Double]("ST_X")
	val getYCoord = SimpleFunction.unary[Geometry, Double]("ST_Y")
	val getZCoord = SimpleFunction.unary[Geometry, Double]("ST_Z")
}