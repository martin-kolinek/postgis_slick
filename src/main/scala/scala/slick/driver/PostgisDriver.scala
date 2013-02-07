package scala.slick.driver

import scala.slick.ast.FieldSymbol
import scala.slick.lifted.postgis._
import scala.slick.lifted.ColumnOption
import scalaz._
import scalaz.std.tuple._
import scalaz.std.option._
import scalaz.std.iterable._
import scalaz.syntax.foldable._
import scalaz.syntax.monoid._
import scalaz.syntax.std.option._
import scalaz.syntax.bitraverse._

trait PostgisDriver extends PostgresDriver {
    override def createColumnDDLBuilder(column:FieldSymbol, table:super.Table[_]) = new GeoColumnDDLBuilder(column)

    def geomTypeMapperDelegate = new GeomTypeMapperDelegate()

    class GeoColumnDDLBuilder(column:FieldSymbol) extends ColumnDDLBuilder(column) {
        def isGeometry = sqlType == "geometry"

        override def handleColumnOption(opt:ColumnOption[_]) = opt match {
            case SRID(_) => 
            case GeometryType(_) =>
            case _ => super.handleColumnOption(opt)
        }

        def getGeoColAttributes(opts:Seq[ColumnOption[_]]) = {
            val default = (4326.some.first, "Geometry".some.first)
            val o = opts.map { x=> x match {
                case SRID(id) => (Some(id), None)
                case GeometryType(t) => (None, Some(t))
                case _ => (None, None)}
            }.map{case (x,y) => (x.first, y.first)}.concatenate |+| default
            o.bisequence[FirstOption, Int, String].get
        }

        override def appendColumn(sb:StringBuilder) {
            if(isGeometry) {
                val (srid, tp) = getGeoColAttributes(column.options)
                sb append quoteIdentifier(column.name) append ' ' append sqlType append s"($tp, $srid)"
            }
            else {
                super.appendColumn(sb)
            }
        }
    }

    class SimpleQL extends super.SimpleQL with TypeMapping {
        trait GeoOptions {
            val GO = new GeoColumnOptions()
        }
    }

    override val simple = new SimpleQL

}

object PostgisDriver extends PostgisDriver
