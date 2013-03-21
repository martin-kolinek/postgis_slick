package scala.slick.lifted.postgis

import scala.slick.driver.PostgresDriver.ColumnDDLBuilder
import scala.slick.ast.FieldSymbol
import scala.slick.lifted.ColumnOption

class GeoColumnDDLBuilder(column:FieldSymbol) extends ColumnDDLBuilder(column) {
    var srid = 4326
    var geotype = "Geometry"
    override def handleColumnOption(o:ColumnOption[_]) {
        o match {
            case GeometryType(t) => geotype = t
            case SRID(id) => srid = id
            case _ => super.handleColumnOption(o)
        }
    }
}
