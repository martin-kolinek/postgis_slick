package scala.slick.postgis

import scala.slick.lifted.SimpleFunction
import scala.slick.driver.PostgresDriver.simple._

trait PostGISConstructorFunctions {
    private def setSRID[T:GeometryType] = SimpleFunction.binary[T, Int, T]("ST_SetSRID").apply _
    private def fixSRID[T:GeometryType](t:Column[T]) = setSRID[T].apply(t, implicitly[GeometryType[T]].srid)
	def makeLine[T:GeometryType](p1:Column[T], p2:Column[T]) = SimpleFunction.binary[T, T, T]("ST_MakeLine").apply(p1, p2)
    def makePoint[T:GeometryType](x:Column[Double], y:Column[Double]) = fixSRID(SimpleFunction.binary[Double, Double, T]("ST_MakePoint").apply(x, y))
    def makePoint[T:GeometryType](x:Column[Double], y:Column[Double], z:Column[Double]) = fixSRID(
        SimpleFunction.ternary[Double, Double, Double, T]("ST_MakePoint").apply(x, y, z))
    def makeBBox[T:GeometryType](p1:Column[T], p2:Column[T]) = SimpleFunction.binary[T,T,T]("ST_MakeBox2D").apply(p1, p2)

    trait ConstImplicits {
        implicit class GeomConst[T:GeometryType](g:Column[T]) {
            def lineTo(other:Column[T]) = makeLine(g, other)
            def bBoxTo(other:Column[T]) = makeBBox(g, other)
        }
    }

}
