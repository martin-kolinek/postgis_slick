package scala.slick.lifted.postgis

import scala.slick.lifted.BaseTypeMapper
import scala.slick.lifted.TypeMapperDelegate
import scala.slick.driver.BasicProfile
import scala.slick.driver.PostgisDriver
import scala.slick.session.PositionedResult
import scala.slick.session.PositionedParameters
import scala.slick.postgis._

trait TypeMapping {
    implicit object GeometryTypeMapper extends BaseTypeMapper[Geometry] {
        def apply(profile:BasicProfile) = profile match {
            case profile:PostgisDriver => profile.geomTypeMapperDelegate
            case _ => sys.error("geometry may only be used with postgis slick driver")
        }
    }
}

class GeomTypeMapperDelegate extends TypeMapperDelegate[Geometry] {
    def zero = null
    def nextValue(r:PositionedResult) = {new Geometry(r.nextObject()); }
    def setOption(v:Option[Geometry], p:PositionedParameters) { v match {
            case None => p.setNull(sqlType)
            case Some(geom) => setValue(geom, p)
        }
    }
    def setValue(v:Geometry, p:PositionedParameters) { p.setObject(v.o, sqlType) }
    def sqlType = java.sql.Types.OTHER
    def sqlTypeName = "geometry"
    def updateValue(v:Geometry, p:PositionedResult) { p.updateObject(v.o) }
}
