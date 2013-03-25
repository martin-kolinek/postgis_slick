package scala.slick.postgis

class Geometry protected[postgis](protected[postgis] val o:Object) {
}

trait GeometryGeomType extends GeometryType[Geometry] {
    def obj(t:Geometry) = t.o
    def fromObj(o:Object) = new Geometry(o)
    def zero = null
}
