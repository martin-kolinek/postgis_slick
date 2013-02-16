package scala.slick

import scala.slick.postgis.TypeMapping

package object postgis extends TypeMapping with PostGisAccessorFunctions with PostGISConstructorFunctions with PostGisPredicates with PostGisMeasurements {
    object implicits extends AccessImplicits with ConstImplicits with PredicateImplicits with MeasurementImplicits {
    }
}
