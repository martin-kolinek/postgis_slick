package scala.slick.postgis

import scala.slick.lifted.BaseTypeMapper
import scala.slick.lifted.TypeMapperDelegate
import scala.slick.driver.BasicProfile
import scala.slick.driver.PostgresDriver
import scala.slick.session.PositionedResult
import scala.slick.session.PositionedParameters

trait TypeMapping {
    implicit def geometryTypeMapper[T:GeometryType] = new BaseTypeMapper[T] {
        def apply(profile:BasicProfile) = profile match {
            case profile:PostgresDriver => new GeomTypeMapperDelegate[T]
            case _ => sys.error("geometry may only be used with postgis slick driver")
        }
    }
}

class GeomTypeMapperDelegate[T:GeometryType] extends TypeMapperDelegate[T] {
    val imp = implicitly[GeometryType[T]]
    def zero = imp.zero
    def nextValue(r:PositionedResult) = imp.fromObj(r.nextObject())
    def setOption(v:Option[T], p:PositionedParameters) { v match {
            case None => p.setNull(sqlType)
            case Some(geom) => setValue(geom, p)
        }
    }
    def setValue(v:T, p:PositionedParameters) { p.setObject(imp.obj(v), sqlType) }
    def sqlType = java.sql.Types.OTHER
    def sqlTypeName = "geometry(%s, %d)".format(imp.geom, imp.srid)
    def updateValue(v:T, p:PositionedResult) { p.updateObject(imp.obj(v)) }
}
