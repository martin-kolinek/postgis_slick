package scala.slick.postgis

trait GeometryType[T] {
    def srid:Int
    def geom:String
    def obj(t:T):Object
    def fromObj(o:Object):T
    def zero:T
}

