package scala.slick.driver

import scala.slick.ast.FieldSymbol
import scala.slick.lifted.postgis._
import scala.slick.lifted.ColumnOption

trait PostgisDriver extends PostgresDriver {
    override def createColumnDDLBuilder(column:FieldSymbol, table:Table[_]) = new GeoColumnDDLBuilder(column)

    def geomTypeMapperDelegate = new GeomTypeMapperDelegate()

    class GeoColumnDDLBuilder(column:FieldSymbol) extends ColumnDDLBuilder(column) {
        def isGeometry = sqlType == "geometry"
        
        var srid = 4326
        var geotype = "Geometry"
        override def handleColumnOption(o:ColumnOption[_]) {
            o match {
                case GeometryType(t) => geotype = t
                case SRID(id) => srid = id
                case _ => super.handleColumnOption(o)
            }
        }

        override def appendColumn(sb:StringBuilder) {
            if(isGeometry) {
                sb append quoteIdentifier(column.name) append ' ' append sqlType append '(' append geotype append ',' append srid append ')'
            }
            else {
                super.appendColumn(sb)
            }
        }
    }
}

