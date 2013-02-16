package scala.slick.postgis

import scala.slick.lifted.SimpleFunction
import scala.slick.driver.PostgresDriver.simple._

trait PostGisMeasurements {
    def distance[T:GeometryType](g1:Column[T], g2:Column[T]) = SimpleFunction.binary[T, T, Double]("ST_Distance").apply(g1, g2)
    
    trait MeasurementImplicits {
        implicit class GeomMeas[T:GeometryType](t:Column[T]) {
            def distanceTo(other:Column[T]) = distance(t, other)
            def <-> (other:Column[T]) = distance(t, other)
        }
    }
}
