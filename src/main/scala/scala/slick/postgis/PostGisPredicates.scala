package scala.slick.postgis

import scala.slick.lifted.SimpleFunction
import scala.slick.lifted.SimpleBinaryOperator
import scala.slick.driver.PostgresDriver.simple._

trait PostGisPredicates {
    def bBox2DIntersection[T:GeometryType](c1:Column[T], c2:Column[T]) = SimpleBinaryOperator[Boolean]("&&").apply(c1, c2)
    def intersecting[T:GeometryType](c1:Column[T], c2:Column[T]) = SimpleFunction.binary[T, T, Boolean]("ST_Intersects").apply(c1, c2)

    trait PredicateImplicits {
        implicit class PredClass[T:GeometryType](t:Column[T]) {
            def && (other:Column[T]) = bBox2DIntersection(t, other)
            def intersects(other:Column[T]) = intersecting(t, other)
        }
    }
}
