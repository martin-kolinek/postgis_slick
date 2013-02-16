package scala.slick.postgis

import scala.slick.lifted.SimpleFunction
import scala.slick.driver.PostgresDriver.simple._

trait PostGisAccessorFunctions {
	def getXCoord[T:GeometryType] = SimpleFunction.unary[T, Double]("ST_X").apply _
	def getYCoord[T:GeometryType] = SimpleFunction.unary[T, Double]("ST_Y").apply _
	def getZCoord[T:GeometryType] = SimpleFunction.unary[T, Double]("ST_Z").apply _
    def getStartPoint[T:GeometryType] = SimpleFunction.unary[T, T]("ST_StartPoint").apply _
    def getEndPoint[T:GeometryType] = SimpleFunction.unary[T, T]("ST_EndPoint").apply _

    trait AccessImplicits {
        implicit class GeomAccess[T:GeometryType](t:Column[T]) {
            def x = getXCoord[T].apply(t)
            def y = getYCoord[T].apply(t)
            def z = getZCoord[T].apply(t)
            def start = getStartPoint[T].apply(t)
            def end = getEndPoint[T].apply(t)
        }
    }

}
