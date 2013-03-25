package scala.slick.postgis

package object srid4326 {
    implicit val srid4326GeometryType = new GeometryGeomType {
        def srid = 4326
        def geom = "Geometry"
    }
}
