package scala.slick.lifted.postgis

import scala.slick.driver.PostgresDriver.BasicColumnOptions
import scala.slick.lifted.ColumnOption

case class SRID(id:Int) extends ColumnOption[Nothing]

case class GeometryType(t:String) extends ColumnOption[Nothing] {}

class GeoColumnOptions {
    def Srid(id:Int) = SRID(id)
    def GeoType(t:String) = GeometryType(t)
}
